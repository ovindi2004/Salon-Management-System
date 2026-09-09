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

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RatingRepository ratingRepository;
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;

    @Override
    public FeedbackDTO saveFeedback(FeedbackDTO dto) {

        if (dto.getAppointmentId() == null) {
            throw new RuntimeException("Appointment ID is required");
        }

        if (dto.getRating() == null ||
                dto.getRating() < 1 ||
                dto.getRating() > 5) {

            throw new RuntimeException("Rating must be between 1 and 5");
        }

        if (dto.getComment() == null ||
                dto.getComment().trim().isEmpty()) {

            throw new RuntimeException("Feedback comment is required");
        }

        if (feedbackRepository.existsByAppointmentAppointmentId(dto.getAppointmentId())) {
            throw new RuntimeException(
                    "Feedback already exists for this appointment"
            );
        }

        Appointment appointment = appointmentRepository
                .findById(dto.getAppointmentId())
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        Customer customer;

        if (dto.getCustomerId() != null) {

            customer = customerRepository
                    .findById(dto.getCustomerId())
                    .orElseThrow(() ->
                            new RuntimeException("Customer not found"));

        } else {

            customer = appointment.getCustomer();

            if (customer == null) {
                throw new RuntimeException(
                        "Customer not found for appointment"
                );
            }
        }

        Feedback feedback = new Feedback();

        feedback.setAppointment(appointment);
        feedback.setCustomer(customer);
        feedback.setComment(dto.getComment().trim());
        feedback.setStatus(FeedbackStatus.PENDING);

        Feedback savedFeedback = feedbackRepository.save(feedback);

        Rating rating = new Rating();
        rating.setFeedback(savedFeedback);
        rating.setRatingValue(dto.getRating());

        ratingRepository.save(rating);

        return convertToDTO(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackDTO getFeedbackById(Long id) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Feedback not found"));

        return convertToDTO(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> getAllFeedback() {

        return feedbackRepository
                .findAllByOrderByFeedbackDateDesc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> getCustomerFeedback(Long customerId) {

        return feedbackRepository
                .findByCustomerCustomerIdOrderByFeedbackDateDesc(customerId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDTO> searchFeedback(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllFeedback();
        }

        return feedbackRepository
                .search(keyword.trim())
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public FeedbackDTO markAsReviewed(Long id) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Feedback not found"));

        feedback.setStatus(FeedbackStatus.REVIEWED);

        Feedback saved = feedbackRepository.save(feedback);

        return convertToDTO(saved);
    }

    @Override
    public void deleteFeedback(Long id) {

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Feedback not found"));

        ratingRepository.findByFeedbackFeedbackId(id)
                .ifPresent(ratingRepository::delete);

        feedbackRepository.delete(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics() {

        List<Feedback> feedbacks =
                feedbackRepository.findAll();

        long total = feedbacks.size();

        Double avg = ratingRepository.getAverageRating();

        if (avg == null) {
            avg = 0.0;
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

        result.put("totalFeedback", total);
        result.put("averageRating",
                Math.round(avg * 10.0) / 10.0);
        result.put("positiveFeedback",
                positivePercentage);
        result.put("positiveCount", positive);
        result.put("pendingReview", pending);

        return result;
    }

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

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getServicePerformance() {

        List<Feedback> feedbacks =
                feedbackRepository.findAll();

        Map<String, List<Integer>> serviceRatings =
                new LinkedHashMap<>();

        for (Feedback feedback : feedbacks) {

            String serviceName = "Unknown Service";

            try {
                if (feedback.getAppointment() != null &&
                        feedback.getAppointment().getService() != null) {

                    serviceName =
                            feedback.getAppointment()
                                    .getService()
                                    .getServiceName();
                }
            } catch (Exception ignored) {
            }

            Optional<Rating> rating =
                    ratingRepository
                            .findByFeedbackFeedbackId(
                                    feedback.getFeedbackId()
                            );

            if (rating.isPresent()) {

                serviceRatings
                        .computeIfAbsent(
                                serviceName,
                                k -> new ArrayList<>()
                        )
                        .add(rating.get().getRatingValue());
            }
        }

        List<Map<String, Object>> result =
                new ArrayList<>();

        for (Map.Entry<String, List<Integer>> entry :
                serviceRatings.entrySet()) {

            List<Integer> ratings = entry.getValue();

            double average =
                    ratings.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0);

            Map<String, Object> item =
                    new LinkedHashMap<>();

            item.put("serviceName", entry.getKey());
            item.put("averageRating",
                    Math.round(average * 10.0) / 10.0);
            item.put("reviewCount", ratings.size());

            result.add(item);
        }

        result.sort((a, b) ->
                Double.compare(
                        (Double) b.get("averageRating"),
                        (Double) a.get("averageRating")
                ));

        return result;
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {

        FeedbackDTO dto = new FeedbackDTO();

        dto.setFeedbackId(feedback.getFeedbackId());

        if (feedback.getAppointment() != null) {

            dto.setAppointmentId(
                    feedback.getAppointment()
                            .getAppointmentId()
            );

            if (feedback.getAppointment().getService() != null) {

                dto.setServiceName(
                        feedback.getAppointment()
                                .getService()
                                .getServiceName()
                );
            }

            if (feedback.getAppointment().getStaff() != null) {

                dto.setStaffName(
                        feedback.getAppointment()
                                .getStaff()
                                .getStaffName()
                );
            }
        }

        if (feedback.getCustomer() != null) {

            dto.setCustomerId(
                    feedback.getCustomer().getCustomerId()
            );

            dto.setCustomerName(
                    feedback.getCustomer().getCustomerName()
            );

            dto.setCustomerEmail(
                    feedback.getCustomer().getCustomerEmail()
            );
        }

        dto.setComment(feedback.getComment());
        dto.setFeedbackDate(feedback.getFeedbackDate());
        dto.setStatus(feedback.getStatus());

        ratingRepository
                .findByFeedbackFeedbackId(
                        feedback.getFeedbackId()
                )
                .ifPresent(rating -> {

                    dto.setRatingId(
                            rating.getRatingId()
                    );

                    dto.setRating(
                            rating.getRatingValue()
                    );
                });

        return dto;
    }
}