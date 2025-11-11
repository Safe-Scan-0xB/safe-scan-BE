package com.springboot.safescan.mapper;



import com.springboot.safescan.domain.CommunityComment;
import com.springboot.safescan.domain.CommunityPost;
import com.springboot.safescan.dto.CommentResponse;
import com.springboot.safescan.dto.PostSummaryResponse;

import java.util.List;

public class PostMapper {
    public static PostSummaryResponse toSummary(CommunityPost p, String categoryName,
                                                int commentCount, List<String> imageUrls) {
        var dto = new PostSummaryResponse();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setExcerpt(p.getContent().length() <= 40 ? p.getContent() : p.getContent().substring(0, 40) + "...");
        dto.setCreatedAt(p.getCreatedAt());
        dto.setViewCount(p.getViewCount());
        dto.setCommentCount(commentCount);
        dto.setCategoryName(categoryName);
        dto.setImageUrls(imageUrls);
        return dto;
    }

    public static CommentResponse toCommentDto(CommunityComment c) {
        var dto = new CommentResponse();
        dto.setId(c.getId());
        dto.setContent(c.getContent());
        dto.setUserId(c.getUser().getUserId());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}