package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    private Long ratingId;

    private Long feedbackId;

    private Integer ratingValue;

    private LocalDate ratingDate;
}