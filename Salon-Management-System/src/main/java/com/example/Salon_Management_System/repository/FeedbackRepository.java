package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Feedback;
import com.example.Salon_Management_System.enumiration.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByAppointmentAppointmentId(Long appointmentId);

    Optional<Feedback> findByAppointmentAppointmentId(Long appointmentId);

    List<Feedback> findByCustomerCustomerIdOrderByFeedbackDateDesc(Long customerId);

    List<Feedback> findByStatusOrderByFeedbackDateDesc(FeedbackStatus status);

    List<Feedback> findAllByOrderByFeedbackDateDesc();

    long countByStatus(FeedbackStatus status);

    long countByFeedbackDateBetween(LocalDate start, LocalDate end);

    @Query("""
        SELECT f
        FROM Feedback f
        WHERE LOWER(f.customer.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(f.comment) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY f.feedbackDate DESC
    """)
    List<Feedback> search(@Param("keyword") String keyword);
    List<Feedback> findByFeedbackDateBetween(LocalDate fromDate, LocalDate toDate);
}