package dev.ea2.fightingGame.ScoringSystem.Repository;

import dev.ea2.fightingGame.ScoringSystem.Entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Score findByPlayerName(String playerName);
}
