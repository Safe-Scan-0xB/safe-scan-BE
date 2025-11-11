package com.springboot.safescan.controller;

import com.springboot.safescan.dto.LoginRequest;
import com.springboot.safescan.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public String login(@RequestBody LoginRequest req) {
        return authService.login(req.userId(), req.password());
    }

    @PostMapping("/signup")
    public String signup(@RequestBody LoginRequest req) {
        return authService.signup(req.userId(), req.password());
    }
}
