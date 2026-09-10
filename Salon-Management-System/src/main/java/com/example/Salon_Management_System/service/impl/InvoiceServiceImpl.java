package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.InvoiceDTO;
import com.example.Salon_Management_System.dto.InvoiceItemDTO;
import com.example.Salon_Management_System.dto.PaymentDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.Invoice;
import com.example.Salon_Management_System.entity.InvoiceItem;
import com.example.Salon_Management_System.entity.Payment;
import com.example.Salon_Management_System.entity.Product;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.enumiration.InvoiceItemType;
import com.example.Salon_Management_System.enumiration.InvoiceStatus;
import com.example.Salon_Management_System.enumiration.PaymentStatus;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.InvoiceRepository;
import com.example.Salon_Management_System.repository.PaymentRepository;
import com.example.Salon_Management_System.repository.ProductRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.service.InvoiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;


    // =========================================================
    // SAVE INVOICE
    // =========================================================

    @Override
    public InvoiceDTO saveInvoice(InvoiceDTO dto) {

        if (dto == null) {
            throw new RuntimeException("Invoice data is required");
        }

        if (dto.getCustomerId() == null) {
            throw new RuntimeException("Customer is required");
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException(
                    "Invoice must contain at least one item"
            );
        }

        // -----------------------------------------------------
        // CUSTOMER
        // -----------------------------------------------------

        Customer customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(
                generateInvoiceNumber()
        );

        invoice.setCustomer(customer);


        // -----------------------------------------------------
        // APPOINTMENT
        // -----------------------------------------------------

        if (dto.getAppointmentId() != null) {

            Appointment appointment = appointmentRepository
                    .findById(dto.getAppointmentId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Appointment not found"
                            )
                    );

            // One appointment = one invoice
            if (invoiceRepository
                    .findByAppointmentAppointmentId(
                            dto.getAppointmentId()
                    )
                    .isPresent()) {

                throw new RuntimeException(
                        "An invoice already exists for this appointment"
                );
            }

            // Appointment customer must match invoice customer
            if (appointment.getCustomer() == null ||
                    !appointment.getCustomer()
                            .getCustomerId()
                            .equals(customer.getCustomerId())) {

                throw new RuntimeException(
                        "Appointment customer and invoice customer do not match"
                );
            }

            invoice.setAppointment(appointment);
        }


        // -----------------------------------------------------
        // BASIC FIELDS
        // -----------------------------------------------------

        mapBasicFields(dto, invoice);


        // -----------------------------------------------------
        // ITEMS
        // -----------------------------------------------------

        invoice.setItems(new ArrayList<>());

        for (InvoiceItemDTO itemDTO : dto.getItems()) {

            InvoiceItem item =
                    createInvoiceItem(itemDTO);

            item.setInvoice(invoice);

            invoice.getItems().add(item);
        }


        // -----------------------------------------------------
        // INITIAL PAYMENT
        // -----------------------------------------------------

        /*
         * Payment is handled separately by PaymentService.
         *
         * Therefore a new invoice starts with:
         *
         * amountPaid = 0
         * balanceDue = totalAmount
         *
         * Later PaymentService will update these values.
         */

        invoice.setAmountPaid(0.0);


        // -----------------------------------------------------
        // CALCULATE
        // -----------------------------------------------------

        calculateInvoice(invoice);


        Invoice savedInvoice =
                invoiceRepository.save(invoice);

        return convertToDTO(savedInvoice);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    public List<InvoiceDTO> getAllInvoices() {

        return invoiceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    public InvoiceDTO getInvoiceById(Long invoiceId) {

        if (invoiceId == null) {
            throw new RuntimeException(
                    "Invoice ID is required"
            );
        }

        Invoice invoice =
                invoiceRepository.findById(invoiceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found"
                                )
                        );

        return convertToDTO(invoice);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public InvoiceDTO updateInvoice(
            Long invoiceId,
            InvoiceDTO dto
    ) {

        if (invoiceId == null) {
            throw new RuntimeException(
                    "Invoice ID is required"
            );
        }

        if (dto == null) {
            throw new RuntimeException(
                    "Invoice data is required"
            );
        }

        Invoice invoice =
                invoiceRepository.findById(invoiceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found"
                                )
                        );


        // -----------------------------------------------------
        // CUSTOMER
        // -----------------------------------------------------

        if (dto.getCustomerId() != null) {

            Customer customer =
                    customerRepository.findById(
                            dto.getCustomerId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Customer not found"
                            )
                    );

            invoice.setCustomer(customer);
        }


        // -----------------------------------------------------
        // APPOINTMENT
        // -----------------------------------------------------

        if (dto.getAppointmentId() != null) {

            Appointment appointment =
                    appointmentRepository.findById(
                            dto.getAppointmentId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Appointment not found"
                            )
                    );


            // Check appointment already belongs to another invoice
            var existingInvoice =
                    invoiceRepository
                            .findByAppointmentAppointmentId(
                                    dto.getAppointmentId()
                            );

            if (existingInvoice.isPresent()
                    && !existingInvoice.get()
                    .getInvoiceId()
                    .equals(invoiceId)) {

                throw new RuntimeException(
                        "An invoice already exists for this appointment"
                );
            }


            // Appointment customer must match invoice customer
            if (appointment.getCustomer() == null ||
                    invoice.getCustomer() == null ||
                    !appointment.getCustomer()
                            .getCustomerId()
                            .equals(
                                    invoice.getCustomer()
                                            .getCustomerId()
                            )) {

                throw new RuntimeException(
                        "Appointment customer and invoice customer do not match"
                );
            }

            invoice.setAppointment(appointment);
        }


        // -----------------------------------------------------
        // BASIC FIELDS
        // -----------------------------------------------------

        mapBasicFields(dto, invoice);


        // -----------------------------------------------------
        // ITEMS
        // -----------------------------------------------------

        if (dto.getItems() != null &&
                !dto.getItems().isEmpty()) {

            invoice.getItems().clear();

            for (InvoiceItemDTO itemDTO :
                    dto.getItems()) {

                InvoiceItem item =
                        createInvoiceItem(itemDTO);

                item.setInvoice(invoice);

                invoice.getItems().add(item);
            }
        }


        // -----------------------------------------------------
        // RECALCULATE
        // -----------------------------------------------------

        /*
         * IMPORTANT:
         *
         * amountPaid is NOT taken from DTO.
         * Existing Payment records remain the source
         * of truth.
         */

        double paidAmount =
                calculatePaidAmount(invoice);

        invoice.setAmountPaid(paidAmount);


        calculateInvoice(invoice);


        Invoice updatedInvoice =
                invoiceRepository.save(invoice);

        return convertToDTO(updatedInvoice);
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void deleteInvoice(Long invoiceId) {

        if (invoiceId == null) {
            throw new RuntimeException(
                    "Invoice ID is required"
            );
        }

        Invoice invoice =
                invoiceRepository.findById(invoiceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invoice not found"
                                )
                        );


        /*
         * Do not delete financial records if payments exist.
         */

        List<Payment> payments =
                paymentRepository.findByInvoiceInvoiceId(
                        invoiceId
                );

        if (payments != null &&
                !payments.isEmpty()) {

            throw new RuntimeException(
                    "Cannot delete invoice because payments already exist"
            );
        }


        invoiceRepository.delete(invoice);
    }


    // =========================================================
    // SEARCH
    // =========================================================

    @Override
    public List<InvoiceDTO> searchInvoices(
            String keyword
    ) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return getAllInvoices();
        }

        return invoiceRepository
                .searchInvoices(keyword.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // GET BY STATUS
    // =========================================================

    @Override
    public List<InvoiceDTO> getInvoicesByStatus(
            InvoiceStatus status
    ) {

        if (status == null) {
            throw new RuntimeException(
                    "Invoice status is required"
            );
        }

        return invoiceRepository
                .findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // DATE RANGE
    // =========================================================

    @Override
    public List<InvoiceDTO> getInvoicesByDateRange(
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate == null ||
                endDate == null) {

            throw new RuntimeException(
                    "Start date and end date are required"
            );
        }

        if (startDate.isAfter(endDate)) {

            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return invoiceRepository
                .findByInvoiceDateBetween(
                        startDate,
                        endDate
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =========================================================
    // STATISTICS
    // =========================================================

    @Override
    public Map<String, Object> getInvoiceStats() {

        Map<String, Object> stats =
                new LinkedHashMap<>();

        Double totalInvoiced =
                invoiceRepository.getTotalInvoiced();

        Double totalPaid =
                invoiceRepository.getTotalPaid();

        Double totalOutstanding =
                invoiceRepository.getTotalOutstanding();

        Long overdue =
                invoiceRepository.countOverdueInvoices();

        Long totalInvoices =
                invoiceRepository.count();


        stats.put(
                "totalInvoices",
                totalInvoices != null
                        ? totalInvoices
                        : 0L
        );

        stats.put(
                "totalInvoiced",
                totalInvoiced != null
                        ? totalInvoiced
                        : 0.0
        );

        stats.put(
                "totalPaid",
                totalPaid != null
                        ? totalPaid
                        : 0.0
        );

        stats.put(
                "totalOutstanding",
                totalOutstanding != null
                        ? totalOutstanding
                        : 0.0
        );

        stats.put(
                "overdueInvoices",
                overdue != null
                        ? overdue
                        : 0L
        );

        stats.put(
                "paidInvoices",
                invoiceRepository.countByInvoiceStatus(
                        InvoiceStatus.PAID
                )
        );

        stats.put(
                "partialInvoices",
                invoiceRepository.countByInvoiceStatus(
                        InvoiceStatus.PARTIAL
                )
        );

        stats.put(
                "unpaidInvoices",
                invoiceRepository.countByInvoiceStatus(
                        InvoiceStatus.UNPAID
                )
        );

        return stats;
    }


    // =========================================================
    // MAP BASIC FIELDS
    // =========================================================

    private void mapBasicFields(
            InvoiceDTO dto,
            Invoice invoice
    ) {

        invoice.setInvoiceDate(
                dto.getInvoiceDate() != null
                        ? dto.getInvoiceDate()
                        : LocalDate.now()
        );

        invoice.setDueDate(
                dto.getDueDate()
        );


        // If not provided, take current customer details
        if (dto.getCustomerPhone() != null) {

            invoice.setCustomerPhone(
                    dto.getCustomerPhone()
            );

        } else if (invoice.getCustomer() != null) {

            invoice.setCustomerPhone(
                    invoice.getCustomer()
                            .getCustomerPhone()
            );
        }


        if (dto.getBillingAddress() != null) {

            invoice.setBillingAddress(
                    dto.getBillingAddress()
            );

        } else if (invoice.getCustomer() != null) {

            invoice.setBillingAddress(
                    invoice.getCustomer()
                            .getCustomerAddress()
            );
        }


        invoice.setDiscountPercent(
                dto.getDiscountPercent() != null
                        ? dto.getDiscountPercent()
                        : 0.0
        );

        invoice.setTaxRate(
                dto.getTaxRate() != null
                        ? dto.getTaxRate()
                        : 0.0
        );

        invoice.setPaymentMethod(
                dto.getPaymentMethod()
        );

        invoice.setNotes(
                dto.getNotes()
        );
    }


    // =========================================================
    // CREATE INVOICE ITEM
    // =========================================================

    private InvoiceItem createInvoiceItem(
            InvoiceItemDTO dto
    ) {

        if (dto == null) {
            throw new RuntimeException(
                    "Invoice item is required"
            );
        }

        if (dto.getItemType() == null) {
            throw new RuntimeException(
                    "Item type is required"
            );
        }


        InvoiceItem item =
                new InvoiceItem();

        item.setItemType(
                dto.getItemType()
        );


        // -----------------------------------------------------
        // QUANTITY
        // -----------------------------------------------------

        int quantity =
                dto.getQuantity() != null
                        ? dto.getQuantity()
                        : 1;

        if (quantity <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        item.setQuantity(quantity);


        // =====================================================
        // SERVICE ITEM
        // =====================================================

        if (dto.getItemType()
                == InvoiceItemType.SERVICE) {

            if (dto.getServiceId() == null) {

                throw new RuntimeException(
                        "Service ID is required"
                );
            }


            SalonService service =
                    serviceRepository.findById(
                            dto.getServiceId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Service not found"
                            )
                    );


            item.setService(service);
            item.setProduct(null);

            item.setItemName(
                    service.getServiceName()
            );


            BigDecimal price =
                    service.getPrice();

            if (price == null) {

                throw new RuntimeException(
                        "Service price is not available"
                );
            }

            if (price.compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Service price cannot be negative"
                );
            }


            item.setUnitPrice(
                    price.doubleValue()
            );
        }


        // =====================================================
        // PRODUCT ITEM
        // =====================================================

        else if (dto.getItemType()
                == InvoiceItemType.PRODUCT) {

            if (dto.getProductId() == null) {

                throw new RuntimeException(
                        "Product ID is required"
                );
            }


            Product product =
                    productRepository.findById(
                            dto.getProductId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found"
                            )
                    );


            item.setProduct(product);
            item.setService(null);

            item.setItemName(
                    product.getProductName()
            );


            double unitPrice;


            /*
             * Product invoice price:
             *
             * If frontend sends a price, use it.
             * Otherwise use current selling price.
             */

            if (dto.getUnitPrice() != null) {

                unitPrice =
                        dto.getUnitPrice();

            } else {

                unitPrice =
                        product.getSellingPrice();
            }


            if (unitPrice < 0) {

                throw new RuntimeException(
                        "Product price cannot be negative"
                );
            }


            item.setUnitPrice(unitPrice);
        }


        // =====================================================
        // INVALID TYPE
        // =====================================================

        else {

            throw new RuntimeException(
                    "Item type must be SERVICE or PRODUCT"
            );
        }


        // -----------------------------------------------------
        // ITEM SUBTOTAL
        // -----------------------------------------------------

        item.setSubtotal(
                item.getQuantity()
                        * item.getUnitPrice()
        );

        return item;
    }


    // =========================================================
    // CALCULATE INVOICE
    // =========================================================

    private void calculateInvoice(
            Invoice invoice
    ) {

        double subtotal = 0.0;


        // -----------------------------------------------------
        // SUBTOTAL
        // -----------------------------------------------------

        if (invoice.getItems() != null) {

            for (InvoiceItem item :
                    invoice.getItems()) {

                double itemSubtotal =
                        item.getQuantity()
                                * item.getUnitPrice();

                item.setSubtotal(
                        itemSubtotal
                );

                subtotal += itemSubtotal;
            }
        }

        invoice.setSubtotal(subtotal);


        // -----------------------------------------------------
        // DISCOUNT
        // -----------------------------------------------------

        double discountPercent =
                invoice.getDiscountPercent() != null
                        ? invoice.getDiscountPercent()
                        : 0.0;


        if (discountPercent < 0) {
            discountPercent = 0;
        }

        if (discountPercent > 100) {
            discountPercent = 100;
        }

        invoice.setDiscountPercent(
                discountPercent
        );


        double discountAmount =
                subtotal
                        * discountPercent
                        / 100;


        invoice.setDiscountAmount(
                discountAmount
        );


        // -----------------------------------------------------
        // AFTER DISCOUNT
        // -----------------------------------------------------

        double afterDiscount =
                subtotal - discountAmount;


        // -----------------------------------------------------
        // TAX
        // -----------------------------------------------------

        double taxRate =
                invoice.getTaxRate() != null
                        ? invoice.getTaxRate()
                        : 0.0;


        if (taxRate < 0) {
            taxRate = 0;
        }

        if (taxRate > 100) {
            taxRate = 100;
        }

        invoice.setTaxRate(taxRate);


        double taxAmount =
                afterDiscount
                        * taxRate
                        / 100;


        invoice.setTaxAmount(
                taxAmount
        );


        // -----------------------------------------------------
        // TOTAL
        // -----------------------------------------------------

        double total =
                afterDiscount + taxAmount;


        invoice.setTotalAmount(total);


        // -----------------------------------------------------
        // PAID
        // -----------------------------------------------------

        double paid =
                invoice.getAmountPaid() != null
                        ? invoice.getAmountPaid()
                        : 0.0;


        if (paid < 0) {
            paid = 0;
        }

        if (paid > total) {
            paid = total;
        }

        invoice.setAmountPaid(paid);


        // -----------------------------------------------------
        // BALANCE
        // -----------------------------------------------------

        double balance =
                total - paid;


        if (balance < 0) {
            balance = 0;
        }

        invoice.setBalanceDue(balance);


        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        if (total <= 0) {

            invoice.setStatus(
                    InvoiceStatus.DRAFT
            );

        } else if (paid >= total) {

            invoice.setStatus(
                    InvoiceStatus.PAID
            );

        } else if (paid > 0) {

            invoice.setStatus(
                    InvoiceStatus.PARTIAL
            );

        } else if (
                invoice.getDueDate() != null
                        && invoice.getDueDate()
                        .isBefore(LocalDate.now())
        ) {

            invoice.setStatus(
                    InvoiceStatus.OVERDUE
            );

        } else {

            invoice.setStatus(
                    InvoiceStatus.UNPAID
            );
        }
    }


    // =========================================================
    // CALCULATE PAID AMOUNT FROM PAYMENTS
    // =========================================================

    private double calculatePaidAmount(
            Invoice invoice
    ) {

        if (invoice.getInvoiceId() == null) {
            return 0.0;
        }


        List<Payment> payments =
                paymentRepository
                        .findByInvoiceInvoiceId(
                                invoice.getInvoiceId()
                        );


        double paid = 0.0;


        for (Payment payment : payments) {

            if (payment.getPaymentStatus()
                    == PaymentStatus.PAID) {

                if (payment.getAmount() != null) {

                    paid += payment
                            .getAmount()
                            .doubleValue();
                }
            }
        }


        return paid;
    }


    // =========================================================
    // GENERATE INVOICE NUMBER
    // =========================================================

    private String generateInvoiceNumber() {

        String invoiceNumber;

        do {

            int randomNumber =
                    new Random().nextInt(10000);

            invoiceNumber =
                    "INV-"
                            + LocalDate.now()
                            .toString()
                            .replace("-", "")
                            + "-"
                            + String.format(
                            "%04d",
                            randomNumber
                    );

        } while (
                invoiceRepository
                        .existsByInvoiceNumber(
                                invoiceNumber
                        )
        );

        return invoiceNumber;
    }


    // =========================================================
    // ENTITY → DTO
    // =========================================================

    private InvoiceDTO convertToDTO(
            Invoice invoice
    ) {

        InvoiceDTO dto =
                new InvoiceDTO();


        // -----------------------------------------------------
        // INVOICE
        // -----------------------------------------------------

        dto.setInvoiceId(
                invoice.getInvoiceId()
        );

        dto.setInvoiceNumber(
                invoice.getInvoiceNumber()
        );


        // -----------------------------------------------------
        // APPOINTMENT
        // -----------------------------------------------------

        if (invoice.getAppointment() != null) {

            dto.setAppointmentId(
                    invoice.getAppointment()
                            .getAppointmentId()
            );
        }


        // -----------------------------------------------------
        // CUSTOMER
        // -----------------------------------------------------

        if (invoice.getCustomer() != null) {

            Customer customer =
                    invoice.getCustomer();

            dto.setCustomerId(
                    customer.getCustomerId()
            );

            dto.setCustomerName(
                    customer.getCustomerName()
            );

            dto.setCustomerEmail(
                    customer.getCustomerEmail()
            );
        }


        // -----------------------------------------------------
        // BILLING
        // -----------------------------------------------------

        dto.setCustomerPhone(
                invoice.getCustomerPhone()
        );

        dto.setBillingAddress(
                invoice.getBillingAddress()
        );


        // -----------------------------------------------------
        // DATES
        // -----------------------------------------------------

        dto.setInvoiceDate(
                invoice.getInvoiceDate()
        );

        dto.setDueDate(
                invoice.getDueDate()
        );


        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        dto.setStatus(
                invoice.getStatus()
        );


        // -----------------------------------------------------
        // AMOUNTS
        // -----------------------------------------------------

        dto.setSubtotal(
                invoice.getSubtotal()
        );

        dto.setDiscountPercent(
                invoice.getDiscountPercent()
        );

        dto.setDiscountAmount(
                invoice.getDiscountAmount()
        );

        dto.setTaxRate(
                invoice.getTaxRate()
        );

        dto.setTaxAmount(
                invoice.getTaxAmount()
        );

        dto.setTotalAmount(
                invoice.getTotalAmount()
        );

        dto.setAmountPaid(
                invoice.getAmountPaid()
        );

        dto.setBalanceDue(
                invoice.getBalanceDue()
        );


        // -----------------------------------------------------
        // PAYMENT METHOD
        // -----------------------------------------------------

        dto.setPaymentMethod(
                invoice.getPaymentMethod()
        );


        // -----------------------------------------------------
        // NOTES
        // -----------------------------------------------------

        dto.setNotes(
                invoice.getNotes()
        );


        // -----------------------------------------------------
        // ITEMS
        // -----------------------------------------------------

        List<InvoiceItemDTO> itemDTOs =
                new ArrayList<>();


        if (invoice.getItems() != null) {

            for (InvoiceItem item :
                    invoice.getItems()) {

                InvoiceItemDTO itemDTO =
                        new InvoiceItemDTO();


                itemDTO.setInvoiceItemId(
                        item.getInvoiceItemId()
                );

                itemDTO.setItemType(
                        item.getItemType()
                );

                itemDTO.setItemName(
                        item.getItemName()
                );

                itemDTO.setQuantity(
                        item.getQuantity()
                );

                itemDTO.setUnitPrice(
                        item.getUnitPrice()
                );

                itemDTO.setSubtotal(
                        item.getSubtotal()
                );


                if (item.getService() != null) {

                    itemDTO.setServiceId(
                            item.getService()
                                    .getServiceId()
                    );
                }


                if (item.getProduct() != null) {

                    itemDTO.setProductId(
                            item.getProduct()
                                    .getProductId()
                    );
                }


                itemDTOs.add(itemDTO);
            }
        }


        dto.setItems(itemDTOs);


        // -----------------------------------------------------
        // PAYMENTS
        // -----------------------------------------------------

        List<PaymentDTO> paymentDTOs =
                new ArrayList<>();


        List<Payment> payments =
                paymentRepository
                        .findByInvoiceInvoiceId(
                                invoice.getInvoiceId()
                        );


        for (Payment payment :
                payments) {

            paymentDTOs.add(
                    convertPaymentToDTO(
                            payment
                    )
            );
        }


        dto.setPayments(
                paymentDTOs
        );


        return dto;
    }


    // =========================================================
    // PAYMENT → DTO
    // =========================================================

    private PaymentDTO convertPaymentToDTO(
            Payment payment
    ) {

        PaymentDTO dto =
                new PaymentDTO();


        dto.setPaymentId(
                payment.getPaymentId()
        );


        if (payment.getInvoice() != null) {

            Invoice invoice =
                    payment.getInvoice();

            dto.setInvoiceId(
                    invoice.getInvoiceId()
            );

            dto.setInvoiceNumber(
                    invoice.getInvoiceNumber()
            );


            Appointment appointment =
                    invoice.getAppointment();


            // -------------------------------------------------
            // APPOINTMENT
            // -------------------------------------------------

            if (appointment != null) {

                dto.setAppointmentId(
                        appointment.getAppointmentId()
                );

                dto.setAppointmentDate(
                        appointment.getAppointmentDate()
                );

                dto.setAppointmentTime(
                        appointment.getStartTime() != null
                                ? appointment
                                .getStartTime()
                                .toString()
                                : null
                );


                // ---------------------------------------------
                // CUSTOMER
                // ---------------------------------------------

                if (appointment.getCustomer() != null) {

                    Customer customer =
                            appointment.getCustomer();

                    dto.setCustomerId(
                            customer.getCustomerId()
                    );

                    dto.setCustomerName(
                            customer.getCustomerName()
                    );

                    dto.setCustomerPhone(
                            customer.getCustomerPhone()
                    );
                }


                // ---------------------------------------------
                // SERVICE
                // ---------------------------------------------

                if (appointment.getService() != null) {

                    SalonService service =
                            appointment.getService();

                    dto.setServiceId(
                            service.getServiceId()
                    );

                    dto.setServiceName(
                            service.getServiceName()
                    );

                    dto.setServicePrice(
                            service.getPrice()
                    );
                }


                // ---------------------------------------------
                // STAFF
                // ---------------------------------------------

                if (appointment.getStaff() != null) {

                    dto.setStaffId(
                            appointment.getStaff()
                                    .getStaffId()
                    );

                    dto.setStaffName(
                            appointment.getStaff()
                                    .getStaffName()
                    );
                }
            }
        }


        // -----------------------------------------------------
        // PAYMENT
        // -----------------------------------------------------

        dto.setAmount(
                payment.getAmount()
        );

        dto.setPaymentMethod(
                payment.getPaymentMethod()
        );

        dto.setPaymentStatus(
                payment.getPaymentStatus()
        );

        dto.setPaymentDate(
                payment.getPaymentDate()
        );

        dto.setTransactionReference(
                payment.getTransactionReference()
        );

        dto.setNotes(
                payment.getNotes()
        );


        return dto;
    }
}