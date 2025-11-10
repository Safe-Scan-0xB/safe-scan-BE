package com.springboot.safescan.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Table(name = "community_posts")
@Getter @Setter @NoArgsConstructor
public class CommunityPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 게시글아이디 (Auto)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;         // 카테고리 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;                 // 유저아이디 (작성자)

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "viewCount", nullable = false)
    private int viewCount = 0;

    public void increaseView() { this.viewCount++; }
}
