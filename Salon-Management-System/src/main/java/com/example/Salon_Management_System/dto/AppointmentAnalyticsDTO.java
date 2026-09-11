package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentAnalyticsDTO {

    private long totalAppointments;

    private long scheduledAppointments;

    private long confirmedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private long noShowAppointments;
}