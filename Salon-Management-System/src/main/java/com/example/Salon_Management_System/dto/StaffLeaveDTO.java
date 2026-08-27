package com.example.Salon_Management_System.dto;

import com.example.Salon_Management_System.enumiration.LeaveStatus;
import com.example.Salon_Management_System.enumiration.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffLeaveDTO {
    private Long leaveId;
    private  Long staffId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
}
