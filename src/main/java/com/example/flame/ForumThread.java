package com.example.flame;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ForumThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String judul;

    @Column(columnDefinition = "TEXT")
    private String konten;

    private String username;
    private Long gameId;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    public String getKonten() { return konten; }
    public void setKonten(String konten) { this.konten = konten; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}