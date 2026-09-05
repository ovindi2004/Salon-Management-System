package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.ServiceDTO;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final StaffRepository staffRepository;

    @Override
    public ServiceDTO saveService(ServiceDTO serviceDTO) {
        log.info("Saving service: {}", serviceDTO);
        try{
            if(serviceDTO.getServiceName() == null || serviceDTO.getServiceName().trim().isEmpty()){
                throw new RuntimeException("Service name cannot be null or empty");
            }
            if(serviceDTO.getCategory() == null || serviceDTO.getCategory().trim().isEmpty()){
                throw new RuntimeException("Service category cannot be null or empty");
            }
            if(serviceDTO.getPrice() == null || serviceDTO.getPrice().doubleValue() < 0) {
                throw new RuntimeException("Service price must be greater than 0");
            }
            if(serviceDTO.getDuration() == null || serviceDTO.getDuration() < 0) {
                throw new RuntimeException("Service duration must be greater than 0");
            }
            if (serviceRepository.existsByServiceNameIgnoreCase(
                    serviceDTO.getServiceName().trim())) {

                throw new RuntimeException("Service name already exists");
            }

            SalonService service = new SalonService();
            service.setServiceName(serviceDTO.getServiceName().trim());
            service.setCategory(serviceDTO.getCategory().trim());
            service.setDescription(serviceDTO.getDescription());
            service.setPrice(serviceDTO.getPrice());
            service.setDuration(serviceDTO.getDuration());

            if(serviceDTO.getStatus() == null || serviceDTO.getStatus().trim().isEmpty()){
                service.setStatus("ACTIVE");
            }else {
                service.setStatus(serviceDTO.getStatus());
            }

            if(serviceDTO.getStaffIds() != null && !serviceDTO.getStaffIds().isEmpty()){
                List<Staff> staffList = new ArrayList<>();
                for (Long staffId : serviceDTO.getStaffIds()) {
                    Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));
                    staffList.add(staff);
                }
                service.setStaff(staffList);
            }
            SalonService savedService = serviceRepository.save(service);
            return convertToDTO(savedService);
        }catch (Exception e){
            log.error("Failed to save service: {}", serviceDTO, e);
            throw new RuntimeException("Failed to save service");
        }

    }

    @Override
    public List<ServiceDTO> getAllServices() {
        return List.of();
    }

    @Override
    public ServiceDTO getServiceById(Long serviceId) {
        return null;
    }

    @Override
    public void updateService(ServiceDTO serviceDTO) {

    }

    @Override
    public void deleteService(Long serviceId) {

    }

    @Override
    public List<ServiceDTO> searchServices(String name) {
        return List.of();
    }

    @Override
    public void updateStatus(Long serviceId) {

    }

    @Override
    public void assignStaff(Long serviceId, List<Long> staffIds) {

    }
}
