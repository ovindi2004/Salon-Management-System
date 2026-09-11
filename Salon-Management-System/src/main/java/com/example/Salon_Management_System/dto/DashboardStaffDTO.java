package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStaffDTO {

    private Long staffId;

    private String staffName;

    private String position;

    private long totalAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private long noShowAppointments;
}