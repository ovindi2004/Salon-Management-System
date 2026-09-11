package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.AppointmentAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportAnalyticsDTO;
import com.example.Salon_Management_System.dto.ReportDTO;
import com.example.Salon_Management_System.dto.RevenueAnalyticsDTO;
import com.example.Salon_Management_System.dto.ServicePerformanceDTO;
import com.example.Salon_Management_System.dto.StaffPerformanceDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.Report;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.PaymentRepository;
import com.example.Salon_Management_System.repository.ReportRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;


    // ============================================================
    // SAVE REPORT
    // ============================================================

    @Override
    public ReportDTO saveReport(ReportDTO dto) {

        if (dto == null) {
            throw new RuntimeException("Report data is required");
        }

        if (dto.getReportType() == null ||
                dto.getReportType().trim().isEmpty()) {

            throw new RuntimeException(
                    "Report type is required"
            );
        }

        if (dto.getFromDate() == null) {
            throw new RuntimeException(
                    "From date is required"
            );
        }

        if (dto.getToDate() == null) {
            throw new RuntimeException(
                    "To date is required"
            );
        }

        if (dto.getFromDate().isAfter(dto.getToDate())) {
            throw new RuntimeException(
                    "From date cannot be after To date"
            );
        }

        Report report = new Report();

        report.setReportType(
                dto.getReportType().trim()
        );

        report.setFromDate(
                dto.getFromDate()
        );

        report.setToDate(
                dto.getToDate()
        );

        if (dto.getGeneratedBy() != null &&
                !dto.getGeneratedBy().trim().isEmpty()) {

            report.setGeneratedBy(
                    dto.getGeneratedBy().trim()
            );

        } else {

            report.setGeneratedBy("Admin");
        }

        Report savedReport =
                reportRepository.save(report);

        return convertToDTO(savedReport);
    }


    // ============================================================
    // GET REPORT BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportById(Long reportId) {

        Report report =
                reportRepository.findById(reportId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found"
                                )
                        );

        return convertToDTO(report);
    }


    // ============================================================
    // GET ALL REPORTS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getAllReports() {

        return reportRepository
                .findAllByOrderByGeneratedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ============================================================
    // GET REPORTS BY TYPE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByType(
            String reportType) {

        if (reportType == null ||
                reportType.trim().isEmpty()) {

            return getAllReports();
        }

        return reportRepository
                .findByReportTypeIgnoreCase(
                        reportType.trim()
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ============================================================
    // GET REPORTS BY DATE RANGE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByDateRange(
            LocalDate fromDate,
            LocalDate toDate) {

        validateDateRange(fromDate, toDate);

        return reportRepository
                .findByFromDateGreaterThanEqualAndToDateLessThanEqual(
                        fromDate,
                        toDate
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ============================================================
    // DELETE REPORT
    // ============================================================

    @Override
    public void deleteReport(Long reportId) {

        Report report =
                reportRepository.findById(reportId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Report not found"
                                )
                        );

        reportRepository.delete(report);
    }


    // ============================================================
    // OVERALL ANALYTICS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public ReportAnalyticsDTO getOverallAnalytics(
            LocalDate fromDate,
            LocalDate toDate) {

        validateDateRange(fromDate, toDate);

        List<Appointment> appointments =
                appointmentRepository.findAll()
                        .stream()
                        .filter(appointment ->
                                isDateBetween(
                                        appointment.getAppointmentDate(),
                                        fromDate,
                                        toDate
                                )
                        )
                        .toList();

        List<Payment> payments =
                paymentRepository
                        .findByPaymentDateBetween(
                                fromDate,
                                toDate
                        );

        List<Customer> customers =
                customerRepository.findAll();

        List<Staff> staff =
                staffRepository.findAll();

        List<SalonService> services =
                serviceRepository.findAll();


        // --------------------------------------------------------
        // APPOINTMENT COUNTS
        // --------------------------------------------------------

        long totalAppointments =
                appointments.size();

        long completedAppointments =
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.COMPLETED
                );

        long cancelledAppointments =
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.CANCELLED
                );

        long noShowAppointments =
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.NO_SHOW
                );

        long pendingAppointments =
                appointments.stream()
                        .filter(appointment ->
                                appointment.getStatus() ==
                                        AppointmentStatus.SCHEDULED ||
                                        appointment.getStatus() ==
                                                AppointmentStatus.CONFIRMED
                        )
                        .count();


        // --------------------------------------------------------
        // CUSTOMER COUNTS
        // --------------------------------------------------------

        long totalCustomers =
                customers.size();

        long activeCustomers =
                customers.stream()
                        .filter(customer ->
                                customer.getCustomerStatus() != null &&
                                        customer.getCustomerStatus()
                                                .equalsIgnoreCase("Active")
                        )
                        .count();

        long newCustomers =
                customers.stream()
                        .filter(customer ->
                                customer.getCreatedAt() != null &&
                                        isDateBetween(
                                                customer.getCreatedAt(),
                                                fromDate,
                                                toDate
                                        )
                        )
                        .count();


        // --------------------------------------------------------
        // PAYMENT / REVENUE
        // --------------------------------------------------------

        BigDecimal totalRevenue =
                calculateTotalPaymentAmount(payments);

        BigDecimal paidRevenue =
                calculateAmountByStatus(
                        payments,
                        PaymentStatus.PAID
                );

        BigDecimal pendingAmount =
                calculateAmountByStatus(
                        payments,
                        PaymentStatus.PENDING
                );

        BigDecimal refundedAmount =
                calculateAmountByStatus(
                        payments,
                        PaymentStatus.REFUNDED
                );


        // --------------------------------------------------------
        // CREATE RESULT
        // --------------------------------------------------------

        ReportAnalyticsDTO result =
                new ReportAnalyticsDTO();

        result.setTotalAppointments(
                totalAppointments
        );

        result.setCompletedAppointments(
                completedAppointments
        );

        result.setCancelledAppointments(
                cancelledAppointments
        );

        result.setPendingAppointments(
                pendingAppointments
        );

        result.setNoShowAppointments(
                noShowAppointments
        );

        result.setTotalCustomers(
                totalCustomers
        );

        result.setActiveCustomers(
                activeCustomers
        );

        result.setNewCustomers(
                newCustomers
        );

        result.setTotalRevenue(
                totalRevenue
        );

        result.setPaidRevenue(
                paidRevenue
        );

        result.setPendingAmount(
                pendingAmount
        );

        result.setRefundedAmount(
                refundedAmount
        );

        result.setTotalStaff(
                staff.size()
        );

        result.setTotalServices(
                services.size()
        );

        return result;
    }


    // ============================================================
    // REVENUE ANALYTICS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<RevenueAnalyticsDTO> getRevenueAnalytics(
            LocalDate fromDate,
            LocalDate toDate) {

        validateDateRange(fromDate, toDate);

        List<Payment> payments =
                paymentRepository
                        .findByPaymentDateBetween(
                                fromDate,
                                toDate
                        );

        Map<LocalDate, List<Payment>> groupedPayments =
                new LinkedHashMap<>();

        for (Payment payment : payments) {

            if (payment.getPaymentDate() == null) {
                continue;
            }

            groupedPayments
                    .computeIfAbsent(
                            payment.getPaymentDate(),
                            key -> new ArrayList<>()
                    )
                    .add(payment);
        }

        List<RevenueAnalyticsDTO> result =
                new ArrayList<>();

        for (Map.Entry<LocalDate, List<Payment>> entry :
                groupedPayments.entrySet()) {

            BigDecimal revenue =
                    calculateTotalPaymentAmount(
                            entry.getValue()
                    );

            RevenueAnalyticsDTO dto =
                    new RevenueAnalyticsDTO();

            dto.setDate(entry.getKey());

            dto.setRevenue(revenue);

            dto.setPaymentCount(
                    entry.getValue().size()
            );

            result.add(dto);
        }

        result.sort(
                Comparator.comparing(
                        RevenueAnalyticsDTO::getDate
                )
        );

        return result;
    }


    // ============================================================
    // APPOINTMENT ANALYTICS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public AppointmentAnalyticsDTO getAppointmentAnalytics(
            LocalDate fromDate,
            LocalDate toDate) {

        validateDateRange(fromDate, toDate);

        List<Appointment> appointments =
                appointmentRepository.findAll()
                        .stream()
                        .filter(appointment ->
                                isDateBetween(
                                        appointment.getAppointmentDate(),
                                        fromDate,
                                        toDate
                                )
                        )
                        .toList();

        AppointmentAnalyticsDTO result =
                new AppointmentAnalyticsDTO();

        result.setTotalAppointments(
                appointments.size()
        );

        result.setScheduledAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.SCHEDULED
                )
        );

        result.setConfirmedAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.CONFIRMED
                )
        );

        result.setCompletedAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.COMPLETED
                )
        );

        result.setCancelledAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.CANCELLED
                )
        );

        result.setNoShowAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.NO_SHOW
                )
        );

        return result;
    }


    // ============================================================
    // STAFF PERFORMANCE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<StaffPerformanceDTO> getStaffPerformance(
            LocalDate fromDate,
            LocalDate toDate) {

        validateDateRange(fromDate, toDate);

        List<Appointment> appointments =
                appointmentRepository.findAll()
                        .stream()
                        .filter(appointment ->
                                isDateBetween(
                                        appointment.getAppointmentDate(),
                                        fromDate,
                                        toDate
                                )
                        )
                        .toList();

        List<Staff> staffList =
                staffRepository.findAll();

        List<StaffPerformanceDTO> result =
                new ArrayList<>();

        for (Staff staff : staffList) {

            List<Appointment> staffAppointments =
                    appointments.stream()
                            .filter(appointment ->
                                    appointment.getStaff() != null &&
                                            appointment.getStaff()
                                                    .getStaffId()
                                                    .equals(
                                                            staff.getStaffId()
                                                    )
                            )
                            .toList();

            long total =
                    staffAppointments.size();

            long completed =
                    countAppointmentsByStatus(
                            staffAppointments,
                            AppointmentStatus.COMPLETED
                    );

            long cancelled =
                    countAppointmentsByStatus(
                            staffAppointments,
                            AppointmentStatus.CANCELLED
                    );

            long noShow =
                    countAppointmentsByStatus(
                            staffAppointments,
                            AppointmentStatus.NO_SHOW
                    );

            StaffPerformanceDTO dto =
                    new StaffPerformanceDTO();

            dto.setStaffId(
                    staff.getStaffId()
            );

            dto.setStaffName(
                    staff.getStaffName()
            );

            dto.setPosition(
                    staff.getPosition()
            );

            dto.setTotalAppointments(
                    total
            );

            dto.setCompletedAppointments(
                    completed
            );

            dto.setCancelledAppointments(
                    cancelled
            );

            dto.setNoShowAppointments(
                    noShow
            );

            result.add(dto);
        }

        result.sort(
                Comparator.comparing(
                        StaffPerformanceDTO::getTotalAppointments
                ).reversed()
        );

        return result;
    }


    // ============================================================
    // SERVICE PERFORMANCE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<ServicePerformanceDTO> getServicePerformance(
            LocalDate fromDate,
            LocalDate toDate) {

        validateDateRange(fromDate, toDate);

        List<Appointment> appointments =
                appointmentRepository.findAll()
                        .stream()
                        .filter(appointment ->
                                isDateBetween(
                                        appointment.getAppointmentDate(),
                                        fromDate,
                                        toDate
                                )
                        )
                        .toList();

        List<SalonService> services =
                serviceRepository.findAll();

        List<ServicePerformanceDTO> result =
                new ArrayList<>();

        for (SalonService service : services) {

            List<Appointment> serviceAppointments =
                    appointments.stream()
                            .filter(appointment ->
                                    appointment.getService() != null &&
                                            appointment.getService()
                                                    .getServiceId()
                                                    .equals(
                                                            service.getServiceId()
                                                    )
                            )
                            .toList();

            long total =
                    serviceAppointments.size();

            long completed =
                    countAppointmentsByStatus(
                            serviceAppointments,
                            AppointmentStatus.COMPLETED
                    );

            long cancelled =
                    countAppointmentsByStatus(
                            serviceAppointments,
                            AppointmentStatus.CANCELLED
                    );

            ServicePerformanceDTO dto =
                    new ServicePerformanceDTO();

            dto.setServiceId(
                    service.getServiceId()
            );

            dto.setServiceName(
                    service.getServiceName()
            );

            dto.setCategory(
                    service.getCategory()
            );

            dto.setPrice(
                    service.getPrice()
            );

            dto.setTotalAppointments(
                    total
            );

            dto.setCompletedAppointments(
                    completed
            );

            dto.setCancelledAppointments(
                    cancelled
            );

            result.add(dto);
        }

        result.sort(
                Comparator.comparing(
                        ServicePerformanceDTO::getTotalAppointments
                ).reversed()
        );

        return result;
    }


    // ============================================================
    // HELPER — CONVERT REPORT TO DTO
    // ============================================================

    private ReportDTO convertToDTO(
            Report report) {

        ReportDTO dto =
                new ReportDTO();

        dto.setReportId(
                report.getReportId()
        );

        dto.setReportType(
                report.getReportType()
        );

        dto.setFromDate(
                report.getFromDate()
        );

        dto.setToDate(
                report.getToDate()
        );

        dto.setGeneratedAt(
                report.getGeneratedAt()
        );

        dto.setGeneratedBy(
                report.getGeneratedBy()
        );

        return dto;
    }


    // ============================================================
    // HELPER — DATE RANGE VALIDATION
    // ============================================================

    private void validateDateRange(
            LocalDate fromDate,
            LocalDate toDate) {

        if (fromDate == null ||
                toDate == null) {

            throw new RuntimeException(
                    "From date and To date are required"
            );
        }

        if (fromDate.isAfter(toDate)) {

            throw new RuntimeException(
                    "From date cannot be after To date"
            );
        }
    }


    // ============================================================
    // HELPER — CHECK DATE RANGE
    // ============================================================

    private boolean isDateBetween(
            LocalDate date,
            LocalDate fromDate,
            LocalDate toDate) {

        return date != null &&
                !date.isBefore(fromDate) &&
                !date.isAfter(toDate);
    }


    // ============================================================
    // HELPER — APPOINTMENT STATUS COUNT
    // ============================================================

    private long countAppointmentsByStatus(
            List<Appointment> appointments,
            AppointmentStatus status) {

        return appointments.stream()
                .filter(appointment ->
                        appointment.getStatus() == status
                )
                .count();
    }


    // ============================================================
    // HELPER — TOTAL PAYMENT AMOUNT
    // ============================================================

    private BigDecimal calculateTotalPaymentAmount(
            List<Payment> payments) {

        return payments.stream()
                .filter(payment ->
                        payment.getAmount() != null
                )
                .map(Payment::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


    // ============================================================
    // HELPER — PAYMENT AMOUNT BY STATUS
    // ============================================================

    private BigDecimal calculateAmountByStatus(
            List<Payment> payments,
            PaymentStatus status) {

        return payments.stream()
                .filter(payment ->
                        payment.getPaymentStatus() == status
                )
                .filter(payment ->
                        payment.getAmount() != null
                )
                .map(Payment::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}