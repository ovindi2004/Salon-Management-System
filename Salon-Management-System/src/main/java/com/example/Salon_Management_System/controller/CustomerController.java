package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.CustomerDTO;
import com.example.Salon_Management_System.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")

public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(value = "/save",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.saveCustomer(customerDTO);
        return new CommonResponse(0, "Customer saved successfully");
    }
    @GetMapping(value = "/all",produces =  MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllCustomers() {
        return  new CommonResponse(0,customerService.getAllCustomers(), "All Customers  successfully");
    }
    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateCustomer(@RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerDTO);
        return new CommonResponse(0, customerDTO, "Customer updated successfully");
    }
    @DeleteMapping(value = "/delete/{customerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return new CommonResponse(0, "Customer deleted successfully");

    }
    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchCustomer(@RequestParam String name) {
        return new CommonResponse(0, customerService.searchCustomers(name), "Customer search successfully");
    }


}
