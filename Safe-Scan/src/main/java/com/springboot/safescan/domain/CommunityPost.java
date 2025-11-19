package com.springboot.safescan.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.springboot.safescan.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "community_posts")
@Getter @Setter @NoArgsConstructor
public class CommunityPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 게시글아이디 (Auto)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;         // 카테고리 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                 // 유저아이디 (작성자)

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,      // 부모 저장/삭제 시 자식도 같이
            orphanRemoval = true            // 컬렉션에서 제거되면 DELETE
    )
    private List<CommunityPhoto> photos = new ArrayList<>();

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CommunityComment> replies = new ArrayList<>();

    public void increaseView() { this.viewCount++; }
}
