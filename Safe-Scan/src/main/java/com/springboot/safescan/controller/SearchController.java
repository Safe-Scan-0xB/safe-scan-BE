package com.springboot.safescan.controller;

import com.springboot.safescan.dto.SearchResponse;
import com.springboot.safescan.service.port.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/scan")
    public SearchResponse scanPhishing(@RequestParam("url") String url) {
        return searchService.searchUrl(url);
    }
}
