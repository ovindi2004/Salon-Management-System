package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.ChatbotDTO;
import com.example.Salon_Management_System.dto.CommonResponse;
import com.example.Salon_Management_System.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping(value = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse chat(@RequestBody ChatbotDTO.ChatRequest request) {
        ChatbotDTO.ChatResponse response = chatbotService.processChat(request);
        return new CommonResponse(0, response, "Chat message processed successfully");
    }
}
