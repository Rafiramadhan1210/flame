package com.example.flame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    @PostMapping
    public Game tambahGame(@RequestBody Game game) {
        return gameRepository.save(game);
    }

    @GetMapping
    public List<Game> ambilSemuaGame() {
        return gameRepository.findAll();
    }

    @GetMapping("/rekomendasi")
    public List<Game> dapatkanRekomendasi(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String platform) {

        if (genre != null && platform != null) {
            return gameRepository.findByGenreIgnoreCaseAndPlatformIgnoreCase(genre, platform);
        } else if (genre != null) {
            return gameRepository.findByGenreIgnoreCase(genre);
        } else if (platform != null) {
            return gameRepository.findByPlatformIgnoreCase(platform);
        }
        return gameRepository.findAll();
    }

    @GetMapping("/fetch")
    public String fetchExternalGames(@RequestParam(defaultValue = "1000") int totalGames) {
        int pageSize = 40;
        int totalPages = (int) Math.ceil((double) Math.min(totalGames, 1000) / pageSize);
        int gameDisimpan = 0;
        int gameDiskip = 0;

        try {
            for (int page = 1; page <= totalPages; page++) {
                String url = "https://api.rawg.io/api/games?key=" + rawgApiKey
                           + "&page_size=" + pageSize + "&page=" + page;

                RawgResponseDto response = restTemplate.getForObject(url, RawgResponseDto.class);

                if (response != null && response.getResults() != null) {
                    for (GameExternalDto dto : response.getResults()) {
                        if (gameRepository.existsByJudul(dto.getName())) {
                            gameDiskip++;
                            continue;
                        }

                        Game game = new Game();
                        game.setJudul(dto.getName());
                        game.setRating(dto.getRating());
                        game.setDeskripsi(dto.getDescription_raw() != null ? dto.getDescription_raw() : "-");
                        game.setBackgroundImage(dto.getBackground_image());

                        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
                            game.setGenre(dto.getGenres().get(0).getName());
                        } else {
                            game.setGenre("Unknown");
                        }

                        if (dto.getPlatforms() != null && !dto.getPlatforms().isEmpty()) {
                            game.setPlatform(dto.getPlatforms().get(0).getPlatform().getName());
                        } else {
                            game.setPlatform("Unknown");
                        }

                        gameRepository.save(game);
                        gameDisimpan++;
                    }
                }
            }
            return "Disimpan: " + gameDisimpan + ", Diskip duplikat: " + gameDiskip;

        } catch (Exception e) {
            return "Gagal: " + e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/screenshots")
    public ResponseEntity<String> getScreenshots(@PathVariable Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        String slug = game.getJudul().toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-");

        String url = "https://api.rawg.io/api/games/" + slug + "/screenshots?key=" + rawgApiKey;

        try {
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response != null ? response : "[]");
        } catch (Exception e) {
            return ResponseEntity.ok("[]");
        }
    }
}
