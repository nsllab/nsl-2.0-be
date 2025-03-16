package com.backend.nsl_workspace.controller.auth;

import com.backend.nsl_workspace.entity.User;
import com.backend.nsl_workspace.repository.UserRepository;
import com.backend.nsl_workspace.service.JwtService;
import com.backend.nsl_workspace.service.MemberTypeService;
import com.backend.nsl_workspace.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberTypeService memberTypeService;

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Email and password are required"));
        }

        try {
            // Try to find user by email first
            Optional<User> userOptional = userRepository.findByEmail(email);

            // If not found by email, try username
            if (userOptional.isEmpty()) {
                userOptional = userRepository.findByUsername(email);
            }

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if (passwordEncoder.matches(password, user.getPasswordHash())) {
                    String token = jwtService.generateToken(user.getUserId());

                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("token", token);
                    response.put("email", user.getEmail());
                    response.put("member_type", user.getMemberTypeId());
                    response.put("member_type_name", memberTypeService.getMemberTypeName(user.getMemberTypeId()));
                    response.put("username", user.getUsername());
                    response.put("profileImgPath", user.getProfileImgPath());
                    response.put("lastName", user.getLastName());
                    response.put("firstName", user.getFirstName());
                    response.put("korean_name", user.getKoreanName());
                    response.put("user_id", user.getUserId());

//                    String redirectUrl = switch (user.getRoleId()) {
//                        case 1 -> "/UserPages";
//                        case 2 -> "/UserPages";
//                        case 3 -> "/UserPages";
//                        default -> "/UserPages";
//                    };

                    response.put("redirectUrl", "/UserPages");

                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Incorrect password"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "User not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> payload) {
        try {
            // Required fields
            String firstName = (String) payload.get("firstName");
            String lastName = (String) payload.get("lastName");
            String email = (String) payload.get("email");
            String password = (String) payload.get("password");
            String username = (String) payload.get("username");
            Integer memberTypeId = (Integer) payload.get("memberTypeId");

            // Optional fields
            String middleName = (String) payload.get("middleName");
            String koreanName = (String) payload.get("koreanName");
            String profileImgPath = (String) payload.get("profileImgPath");
            String contInfo = (String) payload.get("contInfo");
            Integer roleId = (Integer) payload.get("roleId");

            // Required field validation
            if (firstName == null || firstName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "First name is required"));
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Last name is required"));
            }
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Email is required"));
            }
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Username is required"));
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Password is required"));
            }
            if (memberTypeId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Member type ID is required"));
            }

            // Check existing user
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Email already registered"));
            }

            if (userRepository.findByUsername(username).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Username already taken"));
            }

            // Generate user_id (YYYYMMDDNN format)
            LocalDate today = LocalDate.now();
            String datePrefix = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            Integer latestId = userRepository.findLatestIdForDate(datePrefix);
            String userId = String.format("%s%02d", datePrefix, (latestId != null ? latestId + 1 : 1));

            // Create and save user
            User user = new User();
            user.setUserId(Integer.valueOf(userId));

            // Required fields
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setUsername(username);
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setMemberTypeId(memberTypeId);
            user.setJoinDate(LocalDate.now());
            user.setCreatedAt(LocalDateTime.now());

            // Optional fields
            user.setMiddleName(middleName != null && !middleName.trim().isEmpty() ? middleName.trim() : null);
            user.setKoreanName(koreanName != null && !koreanName.trim().isEmpty() ? koreanName.trim() : null);
            user.setProfileImgPath(
                    profileImgPath != null && !profileImgPath.trim().isEmpty() ? profileImgPath.trim() : null);
            user.setContInfo(contInfo != null && !contInfo.trim().isEmpty() ? contInfo.trim() : null);
            user.setUpdatedAt(LocalDateTime.now());

            user = userRepository.save(user);

            // Assign default permissions to the new user
            try {
                permissionService.assignDefaultPermissionsToUser(user.getUserId());
            } catch (Exception e) {
                // Log the error but don't prevent user registration
                System.err.println("Error assigning default permissions: " + e.getMessage());
            }

            // Generate token and response
            String token = jwtService.generateToken(user.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("token", token);
            response.put("email", email);
            response.put("username", username);
            response.put("member_type_id", memberTypeId);
            response.put("user_id", userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out successfully"));
    }

    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "context/profile_images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Uploaded Successfully"));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to upload file: " + e.getMessage()));
        }
    }
}