package com.springboot.safescan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "users") // 예약어 피해서 테이블명 users 권장
@Getter @Setter @NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 유저아이디 (Auto)

    @Column(name = "userId", length = 50, nullable = false, unique = true)
    private String userId;             // 로그인 ID

    @Column(name = "password", length = 100, nullable = false)
    private String password;
}