package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private Long serviceId;
    private String serviceName;
    private String category;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private String status;
    private List<Long> staffIds;
}
