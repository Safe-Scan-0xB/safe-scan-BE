package com.springboot.safescan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
    private boolean hasNext;

    public PageResponse(List<T> content, long totalElements, int totalPages, int page, int size, boolean hasNext) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }
}