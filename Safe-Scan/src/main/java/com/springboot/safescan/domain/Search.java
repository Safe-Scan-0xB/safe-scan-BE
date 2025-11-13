package com.springboot.safescan.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "search")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String phishing_url;

    @Column(nullable = false)
    private Long url_count;

    @Column(nullable = false)
    private String state;

    public void setPhishing_url(String phishing_url) { this.phishing_url = phishing_url; }
    public void setUrlCount(Long url_count) { this.url_count = url_count; }
    public void setState(String state) { this.state = state; }
}
