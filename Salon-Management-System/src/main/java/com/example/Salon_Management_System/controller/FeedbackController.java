package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.dto.FeedbackDTO;
import com.example.Salon_Management_System.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveFeedback(@RequestBody FeedbackDTO dto) {
        FeedbackDTO saved = feedbackService.saveFeedback(dto);
        return new CommonResponse(0, saved, "Feedback saved successfully");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllFeedback() {
        List<FeedbackDTO> feedbackList = feedbackService.getAllFeedback();
        return new CommonResponse(0, feedbackList, "All feedback retrieved successfully");
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFeedbackById(@PathVariable Long id) {
        FeedbackDTO feedback = feedbackService.getFeedbackById(id);
        return new CommonResponse(0, feedback, "Feedback retrieved successfully");
    }


    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getCustomerFeedback(@PathVariable Long customerId) {
        List<FeedbackDTO> feedbackList = feedbackService.getCustomerFeedback(customerId);
        return new CommonResponse(0, feedbackList, "Customer feedback retrieved successfully");
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchFeedback(@RequestParam String keyword) {
        List<FeedbackDTO> feedbackList = feedbackService.searchFeedback(keyword);
        return new CommonResponse(0, feedbackList, "Feedback search completed successfully");
    }

    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getStatistics() {
        Map<String, Object> statistics = feedbackService.getStatistics();
        return new CommonResponse(0, statistics, "Feedback statistics retrieved successfully");
    }
    @GetMapping(value = "/rating-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRatingDistribution() {
        Map<Integer, Long> distribution = feedbackService.getRatingDistribution();
        return new CommonResponse(0, distribution, "Rating distribution retrieved successfully");
    }

    @GetMapping(value = "/service-performance", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getServicePerformance() {
        List<Map<String, Object>> performance = feedbackService.getServicePerformance();
        return new CommonResponse(0, performance, "Service performance retrieved successfully");
    }

    @PatchMapping(value = "/{id}/reviewed", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse markAsReviewed(@PathVariable Long id) {
        FeedbackDTO updated = feedbackService.markAsReviewed(id);
        return new CommonResponse(0, updated, "Feedback marked as reviewed");
    }
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return new CommonResponse(0, null, "Feedback deleted successfully");
    }
}