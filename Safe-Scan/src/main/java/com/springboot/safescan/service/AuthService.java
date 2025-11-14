package com.springboot.safescan.service;

import com.springboot.safescan.domain.User;
import com.springboot.safescan.repository.UserRepository;
import com.springboot.safescan.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String signup(String userId, String password) {

        // 1) 중복 아이디 체크
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2) 저장
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);

        userRepository.save(user);
        return "회원가입 완료";
    }

    public String login(String userId, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        // 로그인 성공 → 토큰 생성
        return jwtTokenProvider.createToken(userId);
    }


}



