package com.example.Salon_Management_System.service.impl;

import com.example.Salon_Management_System.dto.ChangePasswordDTO;
import com.example.Salon_Management_System.dto.UserDTO;
import com.example.Salon_Management_System.entity.Customer;
import com.example.Salon_Management_System.entity.User;
import com.example.Salon_Management_System.enumiration.UserRole;
import com.example.Salon_Management_System.enumiration.UserStatus;
import com.example.Salon_Management_System.repository.CustomerRepository;
import com.example.Salon_Management_System.repository.UserRepository;
import com.example.Salon_Management_System.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
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
            user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
            user.isPasswordChanged();
            user.setUserAddress(userDTO.getUserAddress());
            user.setUserDob(userDTO.getUserDob());
            user.setUserGender(userDTO.getUserGender());


            user.setRole(UserRole.CUSTOMER);
            user.setStatus(UserStatus. Active);

            User SaveUser = userRepository.save(user);

            log.info("User saved successfully: {}", user);

            Customer customer = new Customer();

            customer.setCustomerName(userDTO.getUserName());
            customer.setCustomerEmail(userDTO.getUserEmail());
            customer.setCustomerPhone(userDTO.getUserPhone());

            customer.setCustomerAddress(userDTO.getUserAddress());
            customer.setDateOfBirth(userDTO.getUserDob());
            customer.setGender(userDTO.getUserGender());

            customer.setCustomerNotes(null);

            customer.setTotalVisits(0);
            customer.setLastVisitDate(null);
            customer.setCustomerStatus("Active");

            customer.setUser(SaveUser);

            customerRepository.save(customer);

            log.info("Customer saved successfully: {}", customer.getCustomerId());

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
            if(!passwordEncoder.matches(password, user.getUserPassword())){
                throw new RuntimeException("Invalid password");
            }
            return new UserDTO(user.getUserId(),
                    user.getUserName(),
                    user.getUserEmail(),
                    user.getUserPhone(),
                    user.isPasswordChanged(),
                    user.getUserPassword(),
                    user.getUserDob(),
                    user.getUserAddress(),
                    user.getUserGender(),
                    user.getStatus(),
                    user.getRole()
                    );
    }catch (Exception e){
            log.error("Failed to fetch user by userEmail: {}", userEmail, e);
            throw new RuntimeException("Failed to fetch user by userEmail");
        }
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        log.info("Changing password for user: {}", changePasswordDTO.getUserId());
        try{
        if(changePasswordDTO.getUserId() == null){
            throw new RuntimeException("User id is required");
        }
        if(changePasswordDTO.getCurrentPassword()==null || changePasswordDTO.getCurrentPassword().isBlank()){
            throw new RuntimeException("Current password is required");
        }
        if(changePasswordDTO.getNewPassword()==null || changePasswordDTO.getNewPassword().isBlank()){
            throw new RuntimeException("New password is required");
        }

        String newPassword = changePasswordDTO.getNewPassword();
        if(newPassword.length()<8){
            throw new RuntimeException("New password must be at least 8 characters long");

        }
        if(!newPassword.matches(".*[A-Z].*")){
            throw new RuntimeException(" must contain at least one uppercase letter");
        }
        if(!newPassword.matches(".*[a-z].*")){
            throw new RuntimeException(" must contain at least one lowercase letter");
        }
        if(!newPassword.matches(".*[0-9].*")){
            throw new RuntimeException(" must contain at least one number");
        }
        if(!newPassword.matches(".*[^A-Za-z0-9].*")){
            throw new RuntimeException(" must contain at least one special character");
        }

        User user =userRepository.findById(changePasswordDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        boolean currentPasswordCorrect = passwordEncoder.matches(changePasswordDTO.getCurrentPassword(),user.getUserPassword());
        if(!currentPasswordCorrect){
            throw new RuntimeException("Current password is incorrect");
        }
        if(passwordEncoder.matches(newPassword, user.getUserPassword())){
            throw new RuntimeException("New password must be different from current password");
        }
        String encodedpassword = passwordEncoder.encode(newPassword);
        user.setUserPassword(encodedpassword);

        user.setPasswordChanged(true);

        userRepository.save(user);
        log.info("Password changed successfully for user: {}", user.getUserId());

    }catch (Exception e){
        log.error("Failed to change password for user: {}", changePasswordDTO.getUserId(), e);
        throw new RuntimeException("Failed to change password");
    }
    }
}
