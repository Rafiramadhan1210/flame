package com.example.flame;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String pesan;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPesan() { return pesan; }
    public void setPesan(String pesan) { this.pesan = pesan; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}