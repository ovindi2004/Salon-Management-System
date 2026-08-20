package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.CustomerDTO;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.User;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.UserRepository;
import com.example.Salon_Management_System.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving customer: {}", customerDTO);
         User user = userRepository.findById(customerDTO.getUserId())
                 .orElseThrow(() -> new RuntimeException("User not found with ID: " + customerDTO.getUserId()));
        try{
            Customer customer = new Customer();
            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setCustomerEmail(customerDTO.getCustomerEmail());
            customer.setCustomerPhone(customerDTO.getCustomerPhone());
            customer.setCustomerAddress(customerDTO.getAddress());
            customer.setDateOfBirth(customerDTO.getDateOfBirth());
            customer.setGender(customerDTO.getGender());
            customer.setCustomerNotes(customerDTO.getNotes());

            customer.setTotalVisits(customerDTO.getTotalVisits()!=null
                    ?customerDTO.getTotalVisits():0);

            customer.setLastVisitDate(customerDTO.getLastVisit());

            customer.setCustomerStatus(customerDTO.getStatus()!=null
            ?customerDTO.getStatus():"Active");

            customer.setUser(user);
            customerRepository.save(customer);


        }catch (Exception e){
            log.error("Error saving customer: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Getting all customers");
        try{
            List<CustomerDTO> customers = customerRepository.getAllCustomers();
            return customers;

        }catch (Exception e){
            log.error("Error getting all customers: {}", e.getMessage());
            throw e;
        }
    }


    @Override
    public List<CustomerDTO> searchCustomers(String name) {
        log.info("Searching customers by name: {}", name);
        try{
            List<CustomerDTO> customers = customerRepository.searchCustomers(name);
            return customers;

        }catch (Exception e){
            log.error("Error searching customers: {}", e.getMessage());
            throw e;
        }
    }


    @Override
    public void updateCustomer(CustomerDTO customerDTO) {

        log.info("Updating customer: {}", customerDTO);

        try {

            Customer customer = customerRepository
                    .findById(customerDTO.getCustomerId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Customer not found with ID: "
                                            + customerDTO.getCustomerId()
                            )
                    );

            customer.setCustomerName(customerDTO.getCustomerName());
            customer.setCustomerEmail(customerDTO.getCustomerEmail());
            customer.setCustomerPhone(customerDTO.getCustomerPhone());
            customer.setCustomerAddress(customerDTO.getAddress());
            customer.setDateOfBirth(customerDTO.getDateOfBirth());
            customer.setGender(customerDTO.getGender());
            customer.setCustomerNotes(customerDTO.getNotes());

            if (customerDTO.getTotalVisits() != null) {
                customer.setTotalVisits(customerDTO.getTotalVisits());
            }

            customer.setLastVisitDate(customerDTO.getLastVisit());

            if (customerDTO.getStatus() != null) {
                customer.setCustomerStatus(customerDTO.getStatus());
            }


            if (customerDTO.getUserId() != null) {

                User user = userRepository
                        .findById(customerDTO.getUserId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found with ID: "
                                                + customerDTO.getUserId()
                                )
                        );

                customer.setUser(user);
            }

            customerRepository.save(customer);

        } catch (Exception e) {

            log.error(
                    "Error updating customer: {}",
                    e.getMessage()
            );

            throw e;
        }
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);

        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            log.warn("Customer not found with ID: {}", customerId);
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        Customer customer = customerOptional.get();
        customer.setCustomerStatus("InActive");
        customerRepository.save(customer);

    }
}
