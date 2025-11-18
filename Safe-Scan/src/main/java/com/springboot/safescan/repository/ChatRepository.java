package com.springboot.safescan.repository;

import com.springboot.safescan.domain.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findTop20ByUserIdOrderByIdDesc(String userId);
    List<Chat> findByUserIdAndIdLessThanOrderByIdDesc(
            String userId,
            Long cursor,
            Pageable pageable
    );

    List<Chat> findByUserIdAndTimestampAfterOrderByTimestampAsc(
            String userId,
            LocalDateTime after
    );
}
