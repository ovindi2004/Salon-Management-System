package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.ChatbotDTO;

public interface ChatbotService {
    ChatbotDTO.ChatResponse processChat(ChatbotDTO.ChatRequest request);
}
