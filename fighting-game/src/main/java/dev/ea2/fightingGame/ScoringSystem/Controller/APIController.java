package dev.ea2.fightingGame.ScoringSystem.Controller;

import dev.ea2.fightingGame.ScoringSystem.Entity.Score;
import dev.ea2.fightingGame.ScoringSystem.Repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Replace "*" with your specific origin if needed
public class APIController {
    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping("/all")
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    @PostMapping("/add")
    public Score addScore(@RequestBody Score newScore) {
        return scoreRepository.save(newScore);
    }

    // Expected JSON format:
    // {
    //     "playerName": "Player 1",
    //     "result": "Win",
    //     "datePlayed": "2024-12-31T23:59:59"
    // }

    @PutMapping("/update/{id}")
    public Score updateScore(@PathVariable Long id, @RequestBody Score updatedScore) {
        return scoreRepository.findById(id)
                .map(score -> {
                    score.setPlayerName(updatedScore.getPlayerName());
                    score.setResult(updatedScore.getResult());
                    score.setDatePlayed(updatedScore.getDatePlayed());
                    return scoreRepository.save(score);
                })
                .orElseGet(() -> {
                    updatedScore.setId(id);
                    return scoreRepository.save(updatedScore);
                });
    }

    @DeleteMapping("/delete/{id}")
    public void deleteScore(@PathVariable Long id) {
        scoreRepository.deleteById(id);
    }
}