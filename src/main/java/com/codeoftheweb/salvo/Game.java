package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private Date date;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet = new HashSet<>();

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();

    public Game() {}
    public Game(Date date) {
        this.date = date;
    }



    public Set<Score> getScores() { return scores; }

    public void addScores(Score score) { this.scores.add(score); }

    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayerSet = gamePlayerSet;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayerSet;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        this.gamePlayerSet.add(gamePlayer);
    }


    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "{ Game id: "+ id +
                ", created: " + date +
//                ", gamePlayerSet=" + gamePlayerSet +
                '}';
    }
}
