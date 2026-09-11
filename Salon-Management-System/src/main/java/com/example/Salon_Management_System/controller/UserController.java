package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.*;
import com.example.Salon_Management_System.security.JwtUtil;
import com.example.Salon_Management_System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveUser(@RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        return new CommonResponse(0, userDTO, "User saved successfully");
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse login(@RequestBody AuthDTO authDTO) {
        UserDTO userDetails = userService.getUserDetails(authDTO.getUserEmail(), authDTO.getPassword());
        String token = jwtUtil.generateToken(userDetails);
        UserDataDTO userDataDTO = new UserDataDTO();


        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setUserName(userDetails.getUserName());
        userDataDTO.setToken(token);
        userDataDTO.setUserRole(userDetails.getRole());
        return new CommonResponse(0, userDataDTO, "Login successful");
    }


    @PutMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
        return new CommonResponse(0, changePasswordDTO, "Password changed successfully");
    }
}