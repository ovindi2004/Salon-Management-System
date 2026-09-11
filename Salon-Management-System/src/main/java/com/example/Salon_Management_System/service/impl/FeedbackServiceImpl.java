package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.FeedbackDTO;
import com.example.Salon_Management_System.entity.Appointment;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.Feedback;
import com.example.Salon_Management_System.entity.Rating;
import com.example.Salon_Management_System.enumiration.FeedbackStatus;
import com.example.Salon_Management_System.repository.AppointmentRepository;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.FeedbackRepository;
import com.example.Salon_Management_System.repository.RatingRepository;
import com.example.Salon_Management_System.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RatingRepository ratingRepository;
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;


    // ============================================================
    // SAVE FEEDBACK
    // ============================================================

    @Override
    public FeedbackDTO saveFeedback(FeedbackDTO dto) {

        if (dto.getAppointmentId() == null) {
            throw new RuntimeException("Appointment ID is required");
        }

        if (dto.getRating() == null ||
                dto.getRating() < 1 ||
                dto.getRating() > 5) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        if (dto.getComment() == null ||
                dto.getComment().trim().isEmpty()) {

            throw new RuntimeException(
                    "Feedback comment is required"
            );
        }

        // Prevent duplicate feedback for same appointment
        if (feedbackRepository.existsByAppointmentAppointmentId(
                dto.getAppointmentId())) {

            throw new RuntimeException(
                    "Feedback already exists for this appointment"
            );
        }


        // ========================================================
        // FIND APPOINTMENT
        // ========================================================

        Appointment appointment =
                appointmentRepository.findById(dto.getAppointmentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found"
                                ));


        // ========================================================
        // FIND CUSTOMER
        // ========================================================

        Customer customer;

        if (dto.getCustomerId() != null) {

            customer = customerRepository
                    .findById(dto.getCustomerId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Customer not found"
                            ));

        } else {

            customer = appointment.getCustomer();

            if (customer == null) {
                throw new RuntimeException(
                        "Customer not found for appointment"
                );
            }
        }


        // ========================================================
        // CREATE RATING
        // ========================================================

        Rating rating = new Rating();

        rating.setRatingValue(dto.getRating());


        // ========================================================
        // CREATE FEEDBACK
        // ========================================================

        Feedback feedback = new Feedback();

        feedback.setAppointment(appointment);
        feedback.setCustomer(customer);
        feedback.setRating(rating);
        feedback.setComment(dto.getComment().trim());
        feedback.setStatus(FeedbackStatus.PENDING);


        // ========================================================
        // SAVE FEEDBACK
        // ========================================================

        Feedback savedFeedback =
                feedbackRepository.save(feedback);

        return convertToDTO(savedFeedback);
    }


    // ============================================================
    // GET FEEDBACK BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public FeedbackDTO getFeedbackById(Long id) {

        Feedback feedback =
                feedbackRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Feedback not found"
                                ));

        return convertToDTO(feedback);
    }


    // ============================================================
    // GET ALL FEEDBACK
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> getAllFeedback() {

        return feedbackRepository
                .findAllByOrderByFeedbackDateDesc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ============================================================
    // GET CUSTOMER FEEDBACK
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> getCustomerFeedback(
            Long customerId) {

        return feedbackRepository
                .findByCustomerCustomerIdOrderByFeedbackDateDesc(
                        customerId
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ============================================================
    // SEARCH FEEDBACK
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> searchFeedback(
            String keyword) {

        if (keyword == null ||
                keyword.trim().isEmpty()) {

            return getAllFeedback();
        }

        return feedbackRepository
                .search(keyword.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // ============================================================
    // MARK AS REVIEWED
    // ============================================================

    @Override
    public FeedbackDTO markAsReviewed(Long id) {

        Feedback feedback =
                feedbackRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Feedback not found"
                                ));

        feedback.setStatus(
                FeedbackStatus.REVIEWED
        );

        Feedback savedFeedback =
                feedbackRepository.save(feedback);

        return convertToDTO(savedFeedback);
    }


    // ============================================================
    // DELETE FEEDBACK
    // ============================================================

    @Override
    public void deleteFeedback(Long id) {

        Feedback feedback =
                feedbackRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Feedback not found"
                                ));

        /*
         * Rating is owned by Feedback.
         * Because Feedback has:
         *
         * @OneToOne(
         *     cascade = CascadeType.ALL,
         *     orphanRemoval = true
         * )
         *
         * deleting Feedback will also delete Rating.
         */

        feedbackRepository.delete(feedback);
    }


    // ============================================================
    // STATISTICS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {

        List<Feedback> feedbacks =
                feedbackRepository.findAll();

        long total = feedbacks.size();

        Double averageRating =
                ratingRepository.getAverageRating();

        if (averageRating == null) {
            averageRating = 0.0;
        }

        long positive =
                ratingRepository.countPositiveRatings();

        long pending =
                feedbackRepository.countByStatus(
                        FeedbackStatus.PENDING
                );

        long positivePercentage =
                total == 0
                        ? 0
                        : Math.round(
                        ((double) positive / total) * 100
                );


        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put(
                "totalFeedback",
                total
        );

        result.put(
                "averageRating",
                Math.round(
                        averageRating * 10.0
                ) / 10.0
        );

        result.put(
                "positiveFeedback",
                positivePercentage
        );

        result.put(
                "positiveCount",
                positive
        );

        result.put(
                "pendingReview",
                pending
        );

        return result;
    }


    // ============================================================
    // RATING DISTRIBUTION
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> getRatingDistribution() {

        Map<Integer, Long> result =
                new LinkedHashMap<>();

        for (int i = 5; i >= 1; i--) {

            long count =
                    ratingRepository
                            .findByRatingValue(i)
                            .size();

            result.put(i, count);
        }

        return result;
    }


    // ============================================================
    // SERVICE PERFORMANCE
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getServicePerformance() {

        List<Feedback> feedbacks =
                feedbackRepository.findAll();

        Map<String, List<Integer>> serviceRatings =
                new LinkedHashMap<>();


        for (Feedback feedback : feedbacks) {

            String serviceName =
                    "Unknown Service";

            if (feedback.getAppointment() != null &&
                    feedback.getAppointment().getService() != null) {

                serviceName =
                        feedback.getAppointment()
                                .getService()
                                .getServiceName();
            }


            // Rating is directly available from Feedback
            if (feedback.getRating() != null) {

                Integer ratingValue =
                        feedback.getRating()
                                .getRatingValue();

                if (ratingValue != null) {

                    serviceRatings
                            .computeIfAbsent(
                                    serviceName,
                                    key -> new ArrayList<>()
                            )
                            .add(ratingValue);
                }
            }
        }


        List<Map<String, Object>> result =
                new ArrayList<>();


        for (Map.Entry<String, List<Integer>> entry :
                serviceRatings.entrySet()) {

            List<Integer> ratings =
                    entry.getValue();

            double average =
                    ratings.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0);


            Map<String, Object> item =
                    new LinkedHashMap<>();

            item.put(
                    "serviceName",
                    entry.getKey()
            );

            item.put(
                    "averageRating",
                    Math.round(
                            average * 10.0
                    ) / 10.0
            );

            item.put(
                    "reviewCount",
                    ratings.size()
            );

            result.add(item);
        }


        result.sort((a, b) ->
                Double.compare(
                        ((Number) b.get("averageRating"))
                                .doubleValue(),

                        ((Number) a.get("averageRating"))
                                .doubleValue()
                )
        );

        return result;
    }


    // ============================================================
    // ENTITY → DTO
    // ============================================================

    private FeedbackDTO convertToDTO(
            Feedback feedback) {

        FeedbackDTO dto =
                new FeedbackDTO();


        // ========================================================
        // FEEDBACK
        // ========================================================

        dto.setFeedbackId(
                feedback.getFeedbackId()
        );

        dto.setComment(
                feedback.getComment()
        );

        dto.setFeedbackDate(
                feedback.getFeedbackDate()
        );

        dto.setStatus(
                feedback.getStatus()
        );


        // ========================================================
        // APPOINTMENT
        // ========================================================

        if (feedback.getAppointment() != null) {

            Appointment appointment =
                    feedback.getAppointment();

            dto.setAppointmentId(
                    appointment.getAppointmentId()
            );


            // SERVICE

            if (appointment.getService() != null) {

                dto.setServiceName(
                        appointment
                                .getService()
                                .getServiceName()
                );
            }


            // STAFF

            if (appointment.getStaff() != null) {

                dto.setStaffName(
                        appointment
                                .getStaff()
                                .getStaffName()
                );
            }
        }


        // ========================================================
        // CUSTOMER
        // ========================================================

        if (feedback.getCustomer() != null) {

            Customer customer =
                    feedback.getCustomer();

            dto.setCustomerId(
                    customer.getCustomerId()
            );

            dto.setCustomerName(
                    customer.getCustomerName()
            );

            dto.setCustomerEmail(
                    customer.getCustomerEmail()
            );
        }


        // ========================================================
        // RATING
        // ========================================================

        if (feedback.getRating() != null) {

            Rating rating =
                    feedback.getRating();

            dto.setRatingId(
                    rating.getRatingId()
            );

            dto.setRating(
                    rating.getRatingValue()
            );
        }


        return dto;
    }
}