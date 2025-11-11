package com.springboot.safescan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "community_photo")
@Getter @Setter @NoArgsConstructor
public class CommunityPhoto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사진아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private CommunityPost post;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder; // 업로드 순서 보장용
}