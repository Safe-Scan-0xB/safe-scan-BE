package com.springboot.safescan.service.port;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {
    String store(MultipartFile file); // 저장 후 접근 URL 반환
}