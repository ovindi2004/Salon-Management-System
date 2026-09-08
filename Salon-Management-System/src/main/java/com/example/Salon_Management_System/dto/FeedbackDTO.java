package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.FeedbackStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {

    private Long feedbackId;

    private Long appointmentId;

    private Long customerId;

    private String customerName;

    private String customerEmail;

    private String serviceName;

    private String staffName;

    private String comment;

    private LocalDate feedbackDate;

    private FeedbackStatus status;

    private Integer rating;

    private Long ratingId;
}