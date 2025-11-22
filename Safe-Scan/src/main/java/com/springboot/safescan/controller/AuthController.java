package com.springboot.safescan.controller;

import com.springboot.safescan.dto.LoginRequest;
import com.springboot.safescan.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public Map<String, String> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.userId(), req.password());
        return Map.of("token", token);
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody LoginRequest req) {
        String token = authService.signup(req.userId(), req.password());
        return Map.of("token", token);
    }
}
