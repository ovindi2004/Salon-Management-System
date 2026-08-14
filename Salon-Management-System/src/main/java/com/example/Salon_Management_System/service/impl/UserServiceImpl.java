package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.UserDTO;
import com.example.Salon_Management_System.entity.User;
import com.example.Salon_Management_System.enumiration.UserRole;
import com.example.Salon_Management_System.repository.UserRepository;
import com.example.Salon_Management_System.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public void saveUser(UserDTO userDTO) {
        log.info("Saving user: {}", userDTO);
        try{

            if(userDTO.getUserName()==null || userDTO.getUserName().isBlank()){
                throw new RuntimeException("User name is required");
            }
            if (userDTO.getUserEmail()==null || userDTO.getUserEmail().isBlank()){
                throw new RuntimeException("User email is required");
            }
            if (userDTO.getUserPassword()==null || userDTO.getUserPassword().isBlank()){
                throw new RuntimeException("User password is required");
            }
            if (userRepository.existsByUserEmail(userDTO.getUserEmail())) {
                throw new RuntimeException("Email already exists");
            }
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setUserEmail(userDTO.getUserEmail());
            user.setUserPhone(userDTO.getUserPhone());
            user.setUserPassword(userDTO.getUserPassword());

            user.setRole(UserRole.CUSTOMER);

            userRepository.save(user);

            log.info("User saved successfully: {}", user);

        }catch (Exception e){
            log.error("Failed to save user: {}", userDTO, e);
            throw new RuntimeException("Failed to save user");
        }
    }

    @Override
    public UserDTO getUserDetails(String userEmail, String password) {

        log.info("Fetching user by username: {}", userEmail);
        try{
            Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
            if(optionalUser.isEmpty()){
                throw new RuntimeException("User not found");
            }
            User user = optionalUser.get();
            if(!user.getUserPassword().equals(password)){
                throw new RuntimeException("Invalid password");
            }
            return new UserDTO(user.getUserId(),
                    user.getUserName(),
                    user.getUserEmail(),
                    user.getUserPassword(),
                    user.getUserPhone(),
                    user.getRole());
    }catch (Exception e){
            log.error("Failed to fetch user by userEmail: {}", userEmail, e);
            throw new RuntimeException("Failed to fetch user by userEmail");
        }
    }
}
