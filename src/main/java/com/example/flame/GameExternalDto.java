package com.example.flame;

import java.util.List;

public class GameExternalDto {
    private String name;
    private double rating;
    private String description_raw;
    private List<GenreDto> genres;
    private List<PlatformWrapperDto> platforms;

    public static class GenreDto {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class PlatformWrapperDto {
        private PlatformDetailDto platform;
        public PlatformDetailDto getPlatform() { return platform; }
        public void setPlatform(PlatformDetailDto p) { this.platform = p; }
    }

    public static class PlatformDetailDto {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getDescription_raw() { return description_raw; }
    public void setDescription_raw(String d) { this.description_raw = d; }
    public List<GenreDto> getGenres() { return genres; }
    public void setGenres(List<GenreDto> genres) { this.genres = genres; }
    public List<PlatformWrapperDto> getPlatforms() { return platforms; }
    public void setPlatforms(List<PlatformWrapperDto> p) { this.platforms = p; }
    private String background_image; // Tambahkan ini

public String getBackground_image() { return background_image; }
public void setBackground_image(String bg) { this.background_image = bg; }
}