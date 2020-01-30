package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="score_id")
    private Game game;

    private Number result;

    public Score() { }

    public Score(Player player, Game game, Number result) {
        this.player = player;
        this.game = game;
        this.result = result;
        game.addScores(this);
        player.addScore(this);
    }



    public Long getId() {return id;}
    public Player getPlayer() { return player; }
    public String getPlayerName() { return player.getFirstName(); }
    public Game getGame() { return game; }
    public Number getResult() { return result; }

    @Override
    public String toString() {
        return "Score "+id+" => " + player.getFirstName() +" "+ player.getLastName() +
                "," + game +
                ", result : " + result +"}"
                ;
    }
}
