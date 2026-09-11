package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.CommonResponse;
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


    // ============================================================
    // SAVE FEEDBACK
    // POST /api/v1/feedback
    // ============================================================

    @PostMapping
    public ResponseEntity<CommonResponse> saveFeedback(
            @RequestBody FeedbackDTO dto) {

        try {

            FeedbackDTO saved =
                    feedbackService.saveFeedback(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new CommonResponse(
                                    201,
                                    saved,
                                    "Feedback saved successfully"
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new CommonResponse(
                                    400,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // GET ALL FEEDBACK
    // GET /api/v1/feedback
    // ============================================================

    @GetMapping
    public ResponseEntity<CommonResponse> getAllFeedback() {

        try {

            List<FeedbackDTO> feedbackList =
                    feedbackService.getAllFeedback();

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            feedbackList,
                            "Feedback retrieved successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new CommonResponse(
                                    500,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // GET FEEDBACK BY ID
    // GET /api/v1/feedback/{id}
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getFeedbackById(
            @PathVariable Long id) {

        try {

            FeedbackDTO feedback =
                    feedbackService.getFeedbackById(id);

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            feedback,
                            "Feedback retrieved successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            new CommonResponse(
                                    404,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // GET CUSTOMER FEEDBACK
    // GET /api/v1/feedback/customer/{customerId}
    // ============================================================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CommonResponse> getCustomerFeedback(
            @PathVariable Long customerId) {

        try {

            List<FeedbackDTO> feedbackList =
                    feedbackService.getCustomerFeedback(
                            customerId
                    );

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            feedbackList,
                            "Customer feedback retrieved successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new CommonResponse(
                                    400,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // SEARCH FEEDBACK
    // GET /api/v1/feedback/search?keyword=...
    // ============================================================

    @GetMapping("/search")
    public ResponseEntity<CommonResponse> searchFeedback(
            @RequestParam String keyword) {

        try {

            List<FeedbackDTO> feedbackList =
                    feedbackService.searchFeedback(keyword);

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            feedbackList,
                            "Feedback search completed successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new CommonResponse(
                                    400,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // STATISTICS
    // GET /api/v1/feedback/statistics
    // ============================================================

    @GetMapping("/statistics")
    public ResponseEntity<CommonResponse> getStatistics() {

        try {

            Map<String, Object> statistics =
                    feedbackService.getStatistics();

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            statistics,
                            "Feedback statistics retrieved successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new CommonResponse(
                                    500,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // RATING DISTRIBUTION
    // GET /api/v1/feedback/rating-distribution
    // ============================================================

    @GetMapping("/rating-distribution")
    public ResponseEntity<CommonResponse>
    getRatingDistribution() {

        try {

            Map<Integer, Long> distribution =
                    feedbackService.getRatingDistribution();

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            distribution,
                            "Rating distribution retrieved successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new CommonResponse(
                                    500,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // SERVICE PERFORMANCE
    // GET /api/v1/feedback/service-performance
    // ============================================================

    @GetMapping("/service-performance")
    public ResponseEntity<CommonResponse>
    getServicePerformance() {

        try {

            List<Map<String, Object>> performance =
                    feedbackService.getServicePerformance();

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            performance,
                            "Service performance retrieved successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new CommonResponse(
                                    500,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // MARK AS REVIEWED
    // PATCH /api/v1/feedback/{id}/reviewed
    // ============================================================

    @PatchMapping("/{id}/reviewed")
    public ResponseEntity<CommonResponse> markAsReviewed(
            @PathVariable Long id) {

        try {

            FeedbackDTO updated =
                    feedbackService.markAsReviewed(id);

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            updated,
                            "Feedback marked as reviewed"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new CommonResponse(
                                    400,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }


    // ============================================================
    // DELETE FEEDBACK
    // DELETE /api/v1/feedback/{id}
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteFeedback(
            @PathVariable Long id) {

        try {

            feedbackService.deleteFeedback(id);

            return ResponseEntity.ok(
                    new CommonResponse(
                            200,
                            null,
                            "Feedback deleted successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new CommonResponse(
                                    400,
                                    null,
                                    e.getMessage()
                            )
                    );
        }
    }
}