package com.springboot.safescan.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class PostCreateRequest {
    @NotBlank private String title;
    @NotBlank private String content;
    @NotNull  private Long categoryId;           // ERD: FK (category.id)

    @Size(max = 9) private List<MultipartFile> images;
}
