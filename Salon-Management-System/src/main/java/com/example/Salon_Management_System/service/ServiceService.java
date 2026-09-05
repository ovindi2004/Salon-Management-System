package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.ServiceDTO;

import java.util.List;

public interface ServiceService {
    ServiceDTO saveService(ServiceDTO serviceDTO);
    List<ServiceDTO> getAllServices();
    ServiceDTO getServiceById(Long serviceId);
    void updateService(ServiceDTO serviceDTO);
    void deleteService(Long serviceId);
    List<ServiceDTO> searchServices(String name);
    void updateStatus(Long serviceId);
    void assignStaff(Long serviceId, List<Long> staffIds);
}
