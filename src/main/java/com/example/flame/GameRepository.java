package com.example.flame;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByGenreIgnoreCase(String genre);
    List<Game> findByPlatformIgnoreCase(String platform);
    List<Game> findByGenreIgnoreCaseAndPlatformIgnoreCase(String genre, String platform);
    boolean existsByJudul(String judul);
}