package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "salon_service")
@AllArgsConstructor
@NoArgsConstructor
public class SalonService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;


    // ============================================================
    // SERVICE DETAILS
    // ============================================================

    private String serviceName;

    private String category;


    @Column(length = 1000)
    private String description;


    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal price;


    private Integer duration;

    private String status = "Active";


    // ============================================================
    // SERVICE ↔ STAFF
    // Many Services ↔ Many Staff
    // ============================================================

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_staff",
            joinColumns = @JoinColumn(
                    name = "service_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "staff_id"
            )
    )
    private List<Staff> staff =
            new ArrayList<>();


    // ============================================================
    // SERVICE → APPOINTMENTS
    // One Service → Many Appointments
    // ============================================================

    @OneToMany(
            mappedBy = "service"
    )
    private List<Appointment> appointments =
            new ArrayList<>();


    // ============================================================
    // SERVICE → INVOICE ITEMS
    // One Service → Many Invoice Items
    // ============================================================

    @OneToMany(
            mappedBy = "service"
    )
    private List<InvoiceItem> invoiceItems =
            new ArrayList<>();
}