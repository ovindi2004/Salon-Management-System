package com.example.Salon_Management_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ChatbotDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageItem {
        private String role;
        private String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatRequest {
        private String message;
        private List<MessageItem> history;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatResponse {
        private String reply;
        private boolean success;
        private String model;
    }
}
