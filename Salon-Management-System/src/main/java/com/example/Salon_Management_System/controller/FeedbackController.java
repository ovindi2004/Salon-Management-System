package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.FeedbackDTO;
import com.example.Salon_Management_System.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    // CREATE
    @PostMapping
    public ResponseEntity<?> saveFeedback(
            @RequestBody FeedbackDTO dto) {

        try {

            FeedbackDTO saved =
                    feedbackService.saveFeedback(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {

        return ResponseEntity.ok(
                feedbackService.getAllFeedback()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    feedbackService.getFeedbackById(id)
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // CUSTOMER FEEDBACK
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<FeedbackDTO>> getCustomerFeedback(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                feedbackService.getCustomerFeedback(customerId)
        );
    }

    // SEARCH
    @GetMapping("/search")
    public ResponseEntity<List<FeedbackDTO>> searchFeedback(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                feedbackService.searchFeedback(keyword)
        );
    }

    // STATISTICS
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {

        return ResponseEntity.ok(
                feedbackService.getStatistics()
        );
    }

    // RATING DISTRIBUTION
    @GetMapping("/rating-distribution")
    public ResponseEntity<Map<Integer, Long>>
    getRatingDistribution() {

        return ResponseEntity.ok(
                feedbackService.getRatingDistribution()
        );
    }

    // SERVICE PERFORMANCE
    @GetMapping("/service-performance")
    public ResponseEntity<List<Map<String, Object>>>
    getServicePerformance() {

        return ResponseEntity.ok(
                feedbackService.getServicePerformance()
        );
    }

    // MARK REVIEWED
    @PatchMapping("/{id}/reviewed")
    public ResponseEntity<?> markAsReviewed(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    feedbackService.markAsReviewed(id)
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(
            @PathVariable Long id) {

        try {

            feedbackService.deleteFeedback(id);

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Feedback deleted successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }
}