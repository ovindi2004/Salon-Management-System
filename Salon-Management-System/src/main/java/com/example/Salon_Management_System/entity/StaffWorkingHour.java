package com.example.Salon_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffWorkingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workingHourId;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean dayOff;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    @ToString.Exclude
    private Staff staff;
}