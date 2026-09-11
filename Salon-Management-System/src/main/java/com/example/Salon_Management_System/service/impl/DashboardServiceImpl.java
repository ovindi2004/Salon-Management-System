package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.DashboardAppointmentDTO;
import com.example.Salon_Management_System.dto.DashboardDTO;
import com.example.Salon_Management_System.dto.DashboardRevenueDTO;
import com.example.Salon_Management_System.dto.DashboardServiceDTO;
import com.example.Salon_Management_System.dto.DashboardStaffDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.enumiration.AppointmentStatus;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.PaymentRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final PaymentRepository paymentRepository;


    // =========================================================
    // MAIN DASHBOARD
    // =========================================================

    @Override
    public DashboardDTO getDashboard(
            LocalDate fromDate,
            LocalDate toDate
    ) {

        // -----------------------------------------------------
        // DEFAULT DATE RANGE
        // -----------------------------------------------------

        if (fromDate == null) {
            fromDate = LocalDate.now().withDayOfMonth(1);
        }

        if (toDate == null) {
            toDate = LocalDate.now();
        }

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }


        // -----------------------------------------------------
        // GET DATA FROM DATABASE
        // -----------------------------------------------------

        List<Appointment> appointments =
                appointmentRepository.findByAppointmentDateBetween(
                        fromDate,
                        toDate
                );

        List<Payment> payments =
                paymentRepository.findByPaymentDateBetween(
                        fromDate,
                        toDate
                );

        List<Customer> customers =
                customerRepository.findAll();

        List<Staff> staffList =
                staffRepository.findAll();

        List<SalonService> services =
                serviceRepository.findAll();


        // =====================================================
        // CREATE DASHBOARD DTO
        // =====================================================

        DashboardDTO dashboard = new DashboardDTO();

        dashboard.setFromDate(fromDate);
        dashboard.setToDate(toDate);


        // =====================================================
        // REVENUE CALCULATIONS
        // =====================================================

        BigDecimal totalRevenue =
                calculateTotalRevenue(payments);

        BigDecimal paidRevenue =
                calculateRevenueByStatus(
                        payments,
                        PaymentStatus.PAID
                );

        BigDecimal pendingAmount =
                calculateRevenueByStatus(
                        payments,
                        PaymentStatus.PENDING
                );

        BigDecimal refundedAmount =
                calculateRevenueByStatus(
                        payments,
                        PaymentStatus.REFUNDED
                );

        dashboard.setTotalRevenue(totalRevenue);
        dashboard.setPaidRevenue(paidRevenue);
        dashboard.setPendingAmount(pendingAmount);
        dashboard.setRefundedAmount(refundedAmount);


        // =====================================================
        // APPOINTMENT CALCULATIONS
        // =====================================================

        dashboard.setTotalAppointments(
                appointments.size()
        );

        dashboard.setScheduledAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.SCHEDULED
                )
        );

        dashboard.setConfirmedAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.CONFIRMED
                )
        );

        dashboard.setCompletedAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.COMPLETED
                )
        );

        dashboard.setCancelledAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.CANCELLED
                )
        );

        dashboard.setNoShowAppointments(
                countAppointmentsByStatus(
                        appointments,
                        AppointmentStatus.NO_SHOW
                )
        );


        // =====================================================
        // CUSTOMER CALCULATIONS
        // =====================================================

        dashboard.setTotalCustomers(
                customers.size()
        );

        dashboard.setActiveCustomers(
                countActiveCustomers(customers)
        );

        dashboard.setNewCustomers(
                countNewCustomers(
                        customers,
                        fromDate,
                        toDate
                )
        );


        // =====================================================
        // STAFF & SERVICE COUNTS
        // =====================================================

        dashboard.setTotalStaff(
                staffList.size()
        );

        dashboard.setTotalServices(
                services.size()
        );


        // =====================================================
        // TODAY'S DATA
        // =====================================================

        LocalDate today = LocalDate.now();

        List<Appointment> todayAppointmentsList =
                appointmentRepository.findByAppointmentDate(today);

        dashboard.setTodayAppointments(
                todayAppointmentsList.size()
        );

        dashboard.setTodayScheduled(
                countAppointmentsByStatus(
                        todayAppointmentsList,
                        AppointmentStatus.SCHEDULED
                )
        );

        dashboard.setTodayConfirmed(
                countAppointmentsByStatus(
                        todayAppointmentsList,
                        AppointmentStatus.CONFIRMED
                )
        );

        dashboard.setTodayCompleted(
                countAppointmentsByStatus(
                        todayAppointmentsList,
                        AppointmentStatus.COMPLETED
                )
        );

        dashboard.setTodayCancelled(
                countAppointmentsByStatus(
                        todayAppointmentsList,
                        AppointmentStatus.CANCELLED
                )
        );

        dashboard.setTodayNoShow(
                countAppointmentsByStatus(
                        todayAppointmentsList,
                        AppointmentStatus.NO_SHOW
                )
        );


        // -----------------------------------------------------
        // TODAY'S REVENUE
        // -----------------------------------------------------

        List<Payment> todayPayments =
                paymentRepository.findByPaymentDateBetween(
                        today,
                        today
                );

        dashboard.setTodayRevenue(
                calculateTotalRevenue(todayPayments)
        );


        // =====================================================
        // REVENUE ANALYTICS
        // =====================================================

        dashboard.setRevenueAnalytics(
                buildRevenueAnalytics(
                        payments,
                        fromDate,
                        toDate
                )
        );


        // =====================================================
        // TOP STAFF
        // =====================================================

        dashboard.setTopStaff(
                buildStaffPerformance(
                        appointments
                )
        );


        // =====================================================
        // TOP SERVICES
        // =====================================================

        dashboard.setTopServices(
                buildServicePerformance(
                        appointments
                )
        );


        // =====================================================
        // RECENT APPOINTMENTS
        // =====================================================

        dashboard.setRecentAppointments(
                buildRecentAppointments(
                        appointments
                )
        );


        return dashboard;
    }


    // =========================================================
    // REVENUE
    // =========================================================

    private BigDecimal calculateTotalRevenue(
            List<Payment> payments
    ) {

        if (payments == null || payments.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return payments.stream()
                .filter(payment -> payment.getAmount() != null)
                .filter(payment ->
                        payment.getPaymentStatus() == PaymentStatus.PAID
                )
                .map(Payment::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


    private BigDecimal calculateRevenueByStatus(
            List<Payment> payments,
            PaymentStatus status
    ) {

        if (payments == null || payments.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return payments.stream()
                .filter(payment -> payment.getAmount() != null)
                .filter(payment ->
                        payment.getPaymentStatus() == status
                )
                .map(Payment::getAmount)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }


    // =========================================================
    // APPOINTMENT STATUS
    // =========================================================

    private long countAppointmentsByStatus(
            List<Appointment> appointments,
            AppointmentStatus status
    ) {

        if (appointments == null || appointments.isEmpty()) {
            return 0;
        }

        return appointments.stream()
                .filter(appointment ->
                        appointment.getStatus() == status
                )
                .count();
    }


    // =========================================================
    // CUSTOMER CALCULATIONS
    // =========================================================

    private long countActiveCustomers(
            List<Customer> customers
    ) {

        if (customers == null || customers.isEmpty()) {
            return 0;
        }

        return customers.stream()
                .filter(customer ->
                        customer.getCustomerStatus() != null
                )
                .filter(customer ->
                        "Active".equalsIgnoreCase(
                                customer.getCustomerStatus()
                        )
                )
                .count();
    }


    private long countNewCustomers(
            List<Customer> customers,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        if (customers == null || customers.isEmpty()) {
            return 0;
        }

        return customers.stream()
                .filter(customer ->
                        customer.getCreatedAt() != null
                )
                .filter(customer -> {

                    LocalDate createdDate =
                            customer.getCreatedAt();

                    return !createdDate.isBefore(fromDate)
                            && !createdDate.isAfter(toDate);
                })
                .count();
    }


    // =========================================================
    // REVENUE CHART
    // =========================================================

    private List<DashboardRevenueDTO> buildRevenueAnalytics(
            List<Payment> payments,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        Map<LocalDate, List<Payment>> paymentsByDate =
                payments.stream()
                        .filter(payment ->
                                payment.getPaymentDate() != null
                        )
                        .collect(
                                Collectors.groupingBy(
                                        Payment::getPaymentDate,
                                        LinkedHashMap::new,
                                        Collectors.toList()
                                )
                        );

        List<DashboardRevenueDTO> result =
                new ArrayList<>();

        LocalDate currentDate = fromDate;

        while (!currentDate.isAfter(toDate)) {

            List<Payment> dailyPayments =
                    paymentsByDate.getOrDefault(
                            currentDate,
                            new ArrayList<>()
                    );

            BigDecimal dailyRevenue =
                    calculateTotalRevenue(
                            dailyPayments
                    );

            long paymentCount =
                    dailyPayments.stream()
                            .filter(payment ->
                                    payment.getPaymentStatus()
                                            == PaymentStatus.PAID
                            )
                            .count();

            result.add(
                    new DashboardRevenueDTO(
                            currentDate,
                            dailyRevenue,
                            paymentCount
                    )
            );

            currentDate =
                    currentDate.plusDays(1);
        }

        return result;
    }


    // =========================================================
    // STAFF PERFORMANCE
    // =========================================================

    private List<DashboardStaffDTO> buildStaffPerformance(
            List<Appointment> appointments
    ) {

        Map<Staff, List<Appointment>> appointmentsByStaff =
                appointments.stream()
                        .filter(appointment ->
                                appointment.getStaff() != null
                        )
                        .collect(
                                Collectors.groupingBy(
                                        Appointment::getStaff
                                )
                        );

        return appointmentsByStaff.entrySet()
                .stream()
                .map(entry -> {

                    Staff staff = entry.getKey();

                    List<Appointment> staffAppointments =
                            entry.getValue();

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

                    return new DashboardStaffDTO(
                            staff.getStaffId(),
                            staff.getStaffName(),
                            staff.getPosition(),
                            total,
                            completed,
                            cancelled,
                            noShow
                    );
                })
                .sorted(
                        Comparator.comparingLong(
                                DashboardStaffDTO::getTotalAppointments
                        ).reversed()
                )
                .limit(5)
                .collect(Collectors.toList());
    }


    // =========================================================
    // SERVICE PERFORMANCE
    // =========================================================

    private List<DashboardServiceDTO> buildServicePerformance(
            List<Appointment> appointments
    ) {

        Map<SalonService, List<Appointment>> appointmentsByService =
                appointments.stream()
                        .filter(appointment ->
                                appointment.getService() != null
                        )
                        .collect(
                                Collectors.groupingBy(
                                        Appointment::getService
                                )
                        );

        return appointmentsByService.entrySet()
                .stream()
                .map(entry -> {

                    SalonService service =
                            entry.getKey();

                    List<Appointment> serviceAppointments =
                            entry.getValue();

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

                    return new DashboardServiceDTO(
                            service.getServiceId(),
                            service.getServiceName(),
                            service.getCategory(),
                            service.getPrice(),
                            total,
                            completed,
                            cancelled
                    );
                })
                .sorted(
                        Comparator.comparingLong(
                                DashboardServiceDTO::getTotalAppointments
                        ).reversed()
                )
                .limit(5)
                .collect(Collectors.toList());
    }


    // =========================================================
    // RECENT APPOINTMENTS
    // =========================================================

    private List<DashboardAppointmentDTO> buildRecentAppointments(
            List<Appointment> appointments
    ) {

        return appointments.stream()
                .sorted(
                        Comparator
                                .comparing(
                                        Appointment::getAppointmentDate,
                                        Comparator.nullsLast(
                                                Comparator.reverseOrder()
                                        )
                                )
                                .thenComparing(
                                        Appointment::getStartTime,
                                        Comparator.nullsLast(
                                                Comparator.reverseOrder()
                                        )
                                )
                )
                .limit(10)
                .map(this::convertToDashboardAppointmentDTO)
                .collect(Collectors.toList());
    }


    // =========================================================
    // APPOINTMENT → DASHBOARD DTO
    // =========================================================

    private DashboardAppointmentDTO
    convertToDashboardAppointmentDTO(
            Appointment appointment
    ) {

        String customerName = "";

        if (appointment.getCustomer() != null) {
            customerName =
                    appointment
                            .getCustomer()
                            .getCustomerName();
        }


        String serviceName = "";

        if (appointment.getService() != null) {
            serviceName =
                    appointment
                            .getService()
                            .getServiceName();
        }


        String staffName = "";

        if (appointment.getStaff() != null) {
            staffName =
                    appointment
                            .getStaff()
                            .getStaffName();
        }


        String status = "";

        if (appointment.getStatus() != null) {
            status =
                    appointment
                            .getStatus()
                            .name();
        }


        return new DashboardAppointmentDTO(
                appointment.getAppointmentId(),
                customerName,
                serviceName,
                staffName,
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getDuration(),
                status
        );
    }
}