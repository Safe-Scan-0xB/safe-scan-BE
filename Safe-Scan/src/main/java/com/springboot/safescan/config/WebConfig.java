package com.springboot.safescan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 프로젝트 루트 기준 ./uploads 폴더
        String uploadPath = Paths.get("uploads").toFile().getAbsolutePath();

        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:" + uploadPath + "/");
        // 예: /static/xxx.jpg  ->  {프로젝트루트}/uploads/xxx.jpg
    }
}
