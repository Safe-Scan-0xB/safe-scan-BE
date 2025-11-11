package com.springboot.safescan.service.impl;

import com.springboot.safescan.service.port.ImageStoragePort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.UUID;

@Component
public class LocalImageStorage implements ImageStoragePort {
    private final Path root = Path.of("uploads");

    @Override
    public String store(MultipartFile file) {
        try {
            if (!Files.exists(root)) Files.createDirectories(root);
            var name = UUID.randomUUID() + "_" + file.getOriginalFilename();
            var target = root.resolve(name);
            file.transferTo(target);
            return "/static/" + name; // 정적 리소스 매핑 가정
        } catch (Exception e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}