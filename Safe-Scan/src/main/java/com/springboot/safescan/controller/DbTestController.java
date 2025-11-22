package com.springboot.safescan.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbTestController {

    private final JdbcTemplate jdbc;

    public DbTestController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/db-check")
    public String checkDb() {
        try {
            Integer one = jdbc.queryForObject("SELECT 1", Integer.class);
            return (one != null && one == 1) ? "✅ DB 연결 성공" : "❌ DB 연결 이상";
        } catch (Exception e) {
            return "❌ DB 연결 실패: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
}
