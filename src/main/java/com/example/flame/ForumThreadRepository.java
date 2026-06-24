package com.example.flame;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
    List<ForumThread> findByGameIdOrderByCreatedAtDesc(Long gameId);
    List<ForumThread> findByUsernameOrderByCreatedAtDesc(String username);
}