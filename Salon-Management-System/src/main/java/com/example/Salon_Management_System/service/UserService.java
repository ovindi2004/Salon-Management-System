package com.example.Salon_Management_System.service;

import com.example.Salon_Management_System.dto.ChangePasswordDTO;
import com.example.Salon_Management_System.dto.UserDTO;

public interface UserService {
    void saveUser(UserDTO userDTO);

    UserDTO getUserDetails(String userEmail, String password);

    void changePassword(ChangePasswordDTO changePasswordDTO);
}
