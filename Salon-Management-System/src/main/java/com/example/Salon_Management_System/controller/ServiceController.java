package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.ServiceDTO;
import com.example.Salon_Management_System.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceService serviceService;


    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveService(@RequestBody ServiceDTO serviceDTO) {
        ServiceDTO response = serviceService.saveService(serviceDTO);
        return new CommonResponse(0, response, "Service saved successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllServices() {
        return new CommonResponse(0, serviceService.getAllServices(), "All Services successfully");
    }

    @GetMapping(value = "/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getServiceById(@PathVariable Long serviceId) {
        return new CommonResponse(0, serviceService.getServiceById(serviceId), "Service found successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateService(@RequestBody ServiceDTO serviceDTO) {
        serviceService.updateService(serviceDTO);
        return new CommonResponse(0, serviceDTO, "Service updated successfully");
    }

    @DeleteMapping(value = "/delete/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteService(@PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
        return new CommonResponse(0, "Service deleted successfully");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchService(@RequestParam String name) {
        return new CommonResponse(0, serviceService.searchServices(name), "Service search successfully");
    }

    @PatchMapping(value = "/status/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateStatus(@PathVariable Long serviceId) {
        serviceService.updateStatus(serviceId);
        return new CommonResponse(0, "Service status updated successfully");
    }

    @PutMapping(value = "/assign-staff/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse assignStaff(@PathVariable Long serviceId, @RequestBody List<Long> staffIds) {
        serviceService.assignStaff(serviceId, staffIds);
        return new CommonResponse(0, "Staff assigned to service successfully");
    }
}