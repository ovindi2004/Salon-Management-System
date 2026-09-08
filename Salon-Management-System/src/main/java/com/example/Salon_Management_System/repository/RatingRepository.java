package com.example.Salon_Management_System.repository;

import com.example.Salon_Management_System.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByFeedbackFeedbackId(Long feedbackId);

    List<Rating> findByRatingValue(Integer ratingValue);

    @Query("""
        SELECT COALESCE(AVG(r.ratingValue), 0)
        FROM Rating r
    """)
    Double getAverageRating();

    @Query("""
        SELECT COUNT(r)
        FROM Rating r
        WHERE r.ratingValue >= 4
    """)
    Long countPositiveRatings();
}