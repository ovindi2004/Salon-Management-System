package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.ChatbotDTO;
import com.example.Salon_Management_System.entity.Product;
import com.example.Salon_Management_System.entity.SalonService;
import com.example.Salon_Management_System.entity.Staff;
import com.example.Salon_Management_System.repository.ProductRepository;
import com.example.Salon_Management_System.repository.ServiceRepository;
import com.example.Salon_Management_System.repository.StaffRepository;
import com.example.Salon_Management_System.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final ServiceRepository serviceRepository;
    private final ProductRepository productRepository;
    private final StaffRepository staffRepository;
    private final ObjectMapper objectMapper;

    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private static final String OLLAMA_MODEL = "qwen2.5:3b";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public ChatbotDTO.ChatResponse processChat(ChatbotDTO.ChatRequest request) {
        String userMsg = request.getMessage() != null ? request.getMessage().trim() : "";
        if (userMsg.isEmpty()) {
            return new ChatbotDTO.ChatResponse("Please enter a question or topic, and I will be delighted to assist you with Pink Beauty Salon services!", true, OLLAMA_MODEL);
        }

        // Quick privacy filter check before sending to model
        String lowerMsg = userMsg.toLowerCase();
        if (lowerMsg.contains("password") || lowerMsg.contains("salary") || lowerMsg.contains("salaries")
                || lowerMsg.contains("revenue") || lowerMsg.contains("customer phone") || lowerMsg.contains("customer email")
                || lowerMsg.contains("user_password") || lowerMsg.contains("secret key")) {
            return new ChatbotDTO.ChatResponse("I am sorry, but to ensure strict client confidentiality and data security, I cannot share or access private accounts, passwords, personal customer records, or financial information. However, I would be thrilled to assist you with our beauty services, packages, stylist appointments, or retail products!", true, OLLAMA_MODEL);
        }

        try {
            String systemPrompt = buildSystemPrompt();

            ObjectNode root = objectMapper.createObjectNode();
            root.put("model", OLLAMA_MODEL);
            root.put("stream", false);

            ArrayNode messagesArray = root.putArray("messages");

            // 1. System Prompt
            ObjectNode sysNode = messagesArray.addObject();
            sysNode.put("role", "system");
            sysNode.put("content", systemPrompt);

            // 2. Chat History (last 6 messages max for memory without exceeding context)
            if (request.getHistory() != null && !request.getHistory().isEmpty()) {
                int start = Math.max(0, request.getHistory().size() - 6);
                for (int i = start; i < request.getHistory().size(); i++) {
                    ChatbotDTO.MessageItem item = request.getHistory().get(i);
                    if (item.getContent() != null && !item.getContent().trim().isEmpty()) {
                        String role = "assistant".equalsIgnoreCase(item.getRole()) ? "assistant" : "user";
                        ObjectNode histNode = messagesArray.addObject();
                        histNode.put("role", role);
                        histNode.put("content", item.getContent().trim());
                    }
                }
            }

            // 3. Current User Message
            ObjectNode userNode = messagesArray.addObject();
            userNode.put("role", "user");
            userNode.put("content", userMsg);

            // Options for balanced creative & snappy response
            ObjectNode options = root.putObject("options");
            options.put("temperature", 0.7);
            options.put("top_p", 0.9);
            options.put("num_predict", 300);

            String requestBody = objectMapper.writeValueAsString(root);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(90))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode resJson = objectMapper.readTree(httpResponse.body());
                JsonNode messageNode = resJson.path("message");
                String aiReply = messageNode.path("content").asText();

                if (aiReply != null && !aiReply.trim().isEmpty()) {
                    return new ChatbotDTO.ChatResponse(aiReply.trim(), true, OLLAMA_MODEL);
                }
            }

            log.warn("Ollama returned status {}: {}", httpResponse.statusCode(), httpResponse.body());
            return generateFallbackResponse(userMsg);

        } catch (Exception e) {
            log.error("Error communicating with Ollama: {}", e.getMessage());
            return generateFallbackResponse(userMsg);
        }
    }

    private String buildSystemPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are the luxury AI Receptionist and Beauty Consultant for 'Pink Beauty Salon' located in Colombo, Sri Lanka.\n");
        sb.append("Your name is 'Pink Beauty Assistant'.\n");
        sb.append("Tone: Welcoming, warm, polite, elegant, professional, and knowledgeable.\n\n");

        sb.append("=== SALON INFORMATION ===\n");
        sb.append("- Address: 123 Rose Avenue, Beauty District, Colombo, Sri Lanka\n");
        sb.append("- Hotline: +94 11 234 5678\n");
        sb.append("- Hours: Monday – Saturday: 9:00 AM – 7:00 PM | Sunday: 10:00 AM – 5:00 PM\n");
        sb.append("- Booking: Clients can easily schedule appointments via the 'Booking' section on our website or by calling us.\n");
        sb.append("- Currency: All prices are in Sri Lankan Rupees (LKR / Rs.).\n\n");

        sb.append("=== LIVE PUBLIC SERVICES CATALOG (FROM DATABASE) ===\n");
        try {
            List<SalonService> services = serviceRepository.findAll();
            if (services.isEmpty()) {
                sb.append("Hair Styling, Hair Coloring, Facials, Skin Care, Manicure & Pedicure, Bridal Makeup.\n");
            } else {
                for (SalonService s : services) {
                    if ("Inactive".equalsIgnoreCase(s.getStatus())) continue;
                    sb.append(String.format("• %s | Category: %s | Price: LKR %,.2f | Duration: %d min | %s\n",
                            s.getServiceName(),
                            s.getCategory() != null ? s.getCategory() : "General",
                            s.getPrice() != null ? s.getPrice().doubleValue() : 0.0,
                            s.getDuration() != null ? s.getDuration() : 30,
                            s.getDescription() != null ? s.getDescription() : "Expert salon treatment"));
                }
            }
        } catch (Exception ex) {
            sb.append("Signature hair, skincare, and bridal treatments available.\n");
        }
        sb.append("\n");

        sb.append("=== LIVE PUBLIC RETAIL PRODUCTS CATALOG (FROM DATABASE) ===\n");
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                sb.append("Premium salon hair serums, shampoos, and facial treatment creams.\n");
            } else {
                for (Product p : products) {
                    if (p.getStatus() != null && "INACTIVE".equalsIgnoreCase(p.getStatus().name())) continue;
                    sb.append(String.format("• %s (%s) | Category: %s | Price: LKR %,.2f | In Salon Stock: %d | Pick up at salon desk.\n",
                            p.getProductName(),
                            p.getBrand() != null ? p.getBrand() : "Salon Collection",
                            p.getCategory() != null ? p.getCategory() : "Care",
                            p.getSellingPrice() != null ? p.getSellingPrice() : 0.0,
                            p.getStockQuantity() != null ? p.getStockQuantity() : 0));
                }
            }
        } catch (Exception ex) {
            sb.append("Premium hair & skincare retail items available for salon pickup.\n");
        }
        sb.append("\n");

        sb.append("=== BEAUTY SPECIALISTS & STAFF ===\n");
        try {
            List<Staff> staffList = staffRepository.findAll();
            if (staffList.isEmpty()) {
                sb.append("Certified senior stylists, hair colorists, and skin therapists.\n");
            } else {
                for (Staff st : staffList) {
                    if (st.getStatus() != null && "INACTIVE".equalsIgnoreCase(st.getStatus().name())) continue;
                    sb.append(String.format("• %s — %s (Available for appointments)\n",
                            st.getStaffName(),
                            st.getPosition() != null ? st.getPosition() : "Beauty Specialist"));
                }
            }
        } catch (Exception ex) {
            sb.append("Experienced beauty experts and therapists.\n");
        }
        sb.append("\n");

        sb.append("=== STRICT PRIVACY & BEHAVIORAL DIRECTIVES ===\n");
        sb.append("1. CONFIDENTIALITY: You must NEVER reveal or attempt to access customer passwords, personal phone numbers, customer emails, staff salaries, system user credentials, or financial revenue reports. If asked, politely refuse and emphasize customer privacy protection.\n");
        sb.append("2. ADVICE: Offer helpful beauty advice, explain service benefits, recommend suitable treatments based on the client's hair or skin concerns, and guide them on how to book.\n");
        sb.append("3. ACCURACY: Quote prices accurately using LKR as specified in the catalog above.\n");
        sb.append("4. CONCISENESS: Keep answers structured, elegant, concise (around 2 to 4 paragraphs or bullet points), avoiding overly long repetition.\n");

        return sb.toString();
    }

    private ChatbotDTO.ChatResponse generateFallbackResponse(String userMsg) {
        String lower = userMsg.toLowerCase();
        StringBuilder fallback = new StringBuilder();

        if (lower.contains("price") || lower.contains("cost") || lower.contains("how much") || lower.contains("lkr")) {
            fallback.append("Here is an overview of our signature salon services and pricing in LKR:\n\n");
            try {
                List<SalonService> list = serviceRepository.findAll();
                for (SalonService s : list) {
                    if ("Inactive".equalsIgnoreCase(s.getStatus())) continue;
                    fallback.append(String.format("• **%s** (%s): LKR %,.2f (%d mins)\n",
                            s.getServiceName(), s.getCategory(), s.getPrice().doubleValue(), s.getDuration()));
                }
            } catch (Exception ignored) {}
            fallback.append("\nYou can book any of these directly in our **Booking** section!");
        } else if (lower.contains("product") || lower.contains("buy") || lower.contains("shampoo") || lower.contains("shop")) {
            fallback.append("We offer genuine in-salon retail products! Here are items available at our front desk:\n\n");
            try {
                List<Product> list = productRepository.findAll();
                for (Product p : list) {
                    fallback.append(String.format("• **%s** by %s: LKR %,.2f\n",
                            p.getProductName(), p.getBrand(), p.getSellingPrice()));
                }
            } catch (Exception ignored) {}
            fallback.append("\nVisit our salon reception at 123 Rose Avenue, Colombo to purchase!");
        } else if (lower.contains("book") || lower.contains("appointment") || lower.contains("schedule")) {
            fallback.append("Booking an appointment is simple! ✨\n\n1. Scroll down to the **Booking** section on this page.\n2. Choose your preferred service and beauty specialist.\n3. Pick your preferred date and time slot.\n4. Enter your contact details and confirm your reservation!\n\nAlternatively, call our reception directly at **+94 11 234 5678**.");
        } else if (lower.contains("hour") || lower.contains("time") || lower.contains("open") || lower.contains("location") || lower.contains("where")) {
            fallback.append("🌸 **Pink Beauty Salon Details**:\n\n• **Address**: 123 Rose Avenue, Beauty District, Colombo\n• **Hotline**: +94 11 234 5678\n• **Opening Hours**:\n  - Monday – Saturday: 9:00 AM – 7:00 PM\n  - Sunday: 10:00 AM – 5:00 PM\n\nWe look forward to pampering you!");
        } else {
            fallback.append("Hello! 💗 Welcome to Pink Beauty Salon. I am your AI Beauty Assistant. I can help you with:\n\n• Browsing signature salon services & live pricing\n• Hair styling, skin care & bridal treatment advice\n• In-salon retail beauty products\n• Scheduling an appointment with our specialists\n\nHow can I help make your salon experience wonderful today?");
        }

        return new ChatbotDTO.ChatResponse(fallback.toString(), true, "offline-assistant");
    }
}
