package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.dto.CustomerDTO;
import com.example.Salon_Management_System.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("""
        SELECT new com.example.Salon_Management_System.dto.CustomerDTO(
            c.customerId,
            c.customerName,
            c.customerEmail,
            c.customerPhone,
            c.user.userId,
            c.customerAddress,
            c.dateOfBirth,
            c.gender,
            c.customerNotes,
            c.totalVisits,
            c.lastVisitDate,
            c.customerStatus
        )
        FROM Customer c
        """)

    List<CustomerDTO> getAllCustomers();

    @Query("""
        SELECT new com.example.Salon_Management_System.dto.CustomerDTO(
            c.customerId,
            c.customerName,
            c.customerEmail,
            c.customerPhone,
            c.user.userId,
            c.customerAddress,
            c.dateOfBirth,
            c.gender,
            c.customerNotes,
            c.totalVisits,
            c.lastVisitDate,
            c.customerStatus
        )
        FROM Customer c
        WHERE LOWER(c.customerName)
        LIKE LOWER(CONCAT('%', :name, '%'))
        """)
    List<CustomerDTO> searchCustomers( @Param("name") String name);
    List<Customer> findByLastVisitDateBetween(LocalDate fromDate, LocalDate toDate);
}
