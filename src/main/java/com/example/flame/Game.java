package com.example.flame;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String backgroundImage;
    private String judul;
    private String genre;
    private String platform;
    private double rating;
    private String deskripsi;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getBackgroundImage() { return backgroundImage; }
    public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }
}