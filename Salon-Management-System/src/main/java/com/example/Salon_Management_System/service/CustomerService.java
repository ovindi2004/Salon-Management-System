package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO>searchCustomers(String name);
    void updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
}
