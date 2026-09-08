package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.FeedbackDTO;

import java.util.List;
import java.util.Map;

public interface FeedbackService {

    FeedbackDTO saveFeedback(FeedbackDTO dto);

    FeedbackDTO getFeedbackById(Long id);

    List<FeedbackDTO> getAllFeedback();

    List<FeedbackDTO> getCustomerFeedback(Long customerId);

    List<FeedbackDTO> searchFeedback(String keyword);

    FeedbackDTO markAsReviewed(Long id);

    void deleteFeedback(Long id);

    Map<String, Object> getStatistics();

    Map<Integer, Long> getRatingDistribution();

    List<Map<String, Object>> getServicePerformance();
}