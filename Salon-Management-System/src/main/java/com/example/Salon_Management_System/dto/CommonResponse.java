package com.example.Salon_Management_System.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonResponse {
    private int status;
    private Object body;
    private String message;

    public CommonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public CommonResponse(int status, Object body, String message) {
        this.status = status;
        this.message = message;
        this.body = body;
    }
}
