package com.springboot.safescan.repository;

import com.springboot.safescan.domain.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findTop20ByUserIdOrderByIdDesc(String userId, Pageable pageable);
    List<Chat> findByUserIdAndIdLessThanOrderByIdDesc(
            String userId,
            Long cursor,
            Pageable pageable
    );
    List<Chat> findByUserIdAndTimestampAfterOrderByTimestampAsc(
            String userId,
            LocalDateTime after
    );

    // 24시간 지난 메시지 삭제용
    void deleteByTimestampBefore(LocalDateTime before);
}
