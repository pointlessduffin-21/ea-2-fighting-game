package dev.ea2.fightingGame.ScoringSystem.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String playerName;
    private String result;
    private LocalDateTime datePlayed;

    public Score() {
    }

    public Score(String playerName, String result, LocalDateTime datePlayed) {
        this.playerName = playerName;
        this.result = result;
        this.datePlayed = datePlayed;
    }

    public Long getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getDatePlayed() {
        return datePlayed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setDatePlayed(LocalDateTime datePlayed) {
        this.datePlayed = datePlayed;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", result='" + result + '\'' +
                ", datePlayed=" + datePlayed +
                '}';
    }
}
