package com.example.flame;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ForumReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String konten;

    private String username;
    private Long threadId;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKonten() { return konten; }
    public void setKonten(String konten) { this.konten = konten; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getThreadId() { return threadId; }
    public void setThreadId(Long threadId) { this.threadId = threadId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}