package org.sylph.everything.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.sylph.everything.dto.JwtResponse;
import org.sylph.everything.dto.LoginRequest;
import org.sylph.everything.dto.UserRegistrationDto;
import org.sylph.everything.entity.User;
import org.sylph.everything.service.UserService;
import org.sylph.everything.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ApiAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new JwtResponse(token, user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            User user = userService.findByEmail(userDetails.getUsername());

            return ResponseEntity.ok(new JwtResponse(token, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }
}