package com.example.Salon_Management_System.controller;

import com.example.Salon_Management_System.dto.AdminPasswordResponseDTO;
import com.example.Salon_Management_System.dto.AdminUserRequestDTO;
import com.example.Salon_Management_System.dto.AdminUserResponseDTO;
import com.example.Salon_Management_System.entity.User;
import com.example.Salon_Management_System.enumiration.UserRole;
import com.example.Salon_Management_System.enumiration.UserStatus;
import com.example.Salon_Management_System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/admin-users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    @GetMapping(value="/all", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<AdminUserResponseDTO> getAllUsers(){ return userRepository.findAll().stream().map(this::toResponse).toList(); }

    @GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable Long id){
        User u=userRepository.findById(id).orElse(null);
        return u==null ? ResponseEntity.status(404).body(new ErrorResponse("User not found")) : ResponseEntity.ok(toResponse(u));
    }

    @PostMapping(value="/save", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody AdminUserRequestDTO r){
        try { validate(r,false); if(userRepository.existsByUserEmail(r.getEmail())) return ResponseEntity.status(409).body(new ErrorResponse("Email already exists"));
            User u=new User(); apply(r,u,true); return ResponseEntity.ok(toResponse(userRepository.save(u)));
        } catch(IllegalArgumentException e){ return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage())); }
    }

    @PutMapping(value="/update/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody AdminUserRequestDTO r){
        try { validate(r,true); User u=userRepository.findById(id).orElse(null); if(u==null)return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
            var same=userRepository.findByUserEmail(r.getEmail()); if(same.isPresent()&&!same.get().getUserId().equals(id)) return ResponseEntity.status(409).body(new ErrorResponse("Email already exists"));
            apply(r,u,false); return ResponseEntity.ok(toResponse(userRepository.save(u)));
        } catch(IllegalArgumentException e){ return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage())); }
    }

    @PatchMapping(value="/{id}/role", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeRole(@PathVariable Long id,@RequestParam String role){
        User u=userRepository.findById(id).orElse(null); if(u==null)return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        try { UserRole r=parseRole(role); if(r==UserRole.OWNER||r==UserRole.CUSTOMER)return ResponseEntity.badRequest().body(new ErrorResponse("This role cannot be assigned here")); u.setRole(r); return ResponseEntity.ok(toResponse(userRepository.save(u))); }
        catch(IllegalArgumentException e){return ResponseEntity.badRequest().body(new ErrorResponse("Invalid role"));}
    }

    @PatchMapping(value="/{id}/status", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeStatus(@PathVariable Long id,@RequestParam String status){
        User u=userRepository.findById(id).orElse(null); if(u==null)return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        try {u.setStatus(parseStatus(status)); return ResponseEntity.ok(toResponse(userRepository.save(u)));}
        catch(IllegalArgumentException e){return ResponseEntity.badRequest().body(new ErrorResponse("Invalid status"));}
    }

    @DeleteMapping(value="/delete/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        User u=userRepository.findById(id).orElse(null); if(u==null)return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        if(u.getCustomer()!=null||u.getStaff()!=null)return ResponseEntity.status(409).body(new ErrorResponse("Cannot delete a user linked to a customer or staff record"));
        userRepository.delete(u); return ResponseEntity.ok(new ErrorResponse("User deleted successfully"));
    }

    @PostMapping(value="/{id}/reset-password", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@PathVariable Long id){
        User u=userRepository.findById(id).orElse(null); if(u==null)return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
        String p=temporaryPassword(); u.setUserPassword(passwordEncoder.encode(p)); u.setPasswordChanged(false); userRepository.save(u);
        return ResponseEntity.ok(new AdminPasswordResponseDTO(u.getUserName(),p));
    }

    private void validate(AdminUserRequestDTO r,boolean update){
        if(r==null)throw new IllegalArgumentException("Request is required"); if(blank(r.getFullName()))throw new IllegalArgumentException("Full name is required");
        if(blank(r.getUsername()))throw new IllegalArgumentException("Username is required"); if(blank(r.getEmail()))throw new IllegalArgumentException("Email is required");
        if(blank(r.getRole()))throw new IllegalArgumentException("Role is required"); if(!update&&blank(r.getPassword()))throw new IllegalArgumentException("Password is required");
        UserRole role=parseRole(r.getRole()); if(role==UserRole.OWNER||role==UserRole.CUSTOMER)throw new IllegalArgumentException("Admin can create only Staff or Receptionist");
        if(!blank(r.getStatus()))parseStatus(r.getStatus());
    }
    private void apply(AdminUserRequestDTO r,User u,boolean create){
        // The existing UserService methods are untouched. This controller only adapts this page's field names to User.
        u.setUserName(r.getUsername()); u.setUserEmail(r.getEmail()); u.setUserPhone(r.getPhone()); u.setUserAddress(r.getAddress());
        u.setRole(parseRole(r.getRole())); u.setStatus(blank(r.getStatus())?UserStatus.Active:parseStatus(r.getStatus()));
        if(create){u.setUserPassword(passwordEncoder.encode(r.getPassword()));u.setPasswordChanged(false);}
    }
    private AdminUserResponseDTO toResponse(User u){
        String n=u.getUserName(); return new AdminUserResponseDTO(u.getUserId(),n,n,u.getUserEmail(),u.getRole()==null?null:u.getRole().name(),u.getStatus()==null?null:u.getStatus().name().toUpperCase(Locale.ROOT),u.getUserPhone(),u.getUserAddress(),"-");
    }
    private UserRole parseRole(String s){return UserRole.valueOf(s.trim().toUpperCase(Locale.ROOT));}
    private UserStatus parseStatus(String s){if(s.equalsIgnoreCase("ACTIVE"))return UserStatus.Active;if(s.equalsIgnoreCase("INACTIVE"))return UserStatus.Inactive;return UserStatus.valueOf(s);}
    private String temporaryPassword(){String c="ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%",p="";for(int i=0;i<10;i++)p+=c.charAt(random.nextInt(c.length()));return p;}
    private boolean blank(String s){return s==null||s.isBlank();}
    private record ErrorResponse(String message){}
}
