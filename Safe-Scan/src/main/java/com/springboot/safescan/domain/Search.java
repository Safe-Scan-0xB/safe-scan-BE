package com.springboot.safescan.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "search")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 자바 필드명은 스네이크 케이스 유지
    @Column(name = "phishing_url")
    private String phishing_url;

    @Column(name = "url_count")
    private Long url_count;

    @Column(name = "state")
    private String state;

    public void setPhishing_url(String phishing_url) { this.phishing_url = phishing_url; }
    public void setUrl_count(Long url_count) { this.url_count = url_count; }
    public void setState(String state) { this.state = state; }
}
