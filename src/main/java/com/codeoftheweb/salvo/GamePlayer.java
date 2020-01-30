package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ship = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvo = new HashSet<>();

//private Set<String> hits = new HashSet<>();

    public GamePlayer() {

    }
    public GamePlayer(Player player, Game game, Date date) {
        player.addGamePlayer(this);
        game.addGamePlayer(this);
        this.player = player;
        this.game = game;
        this.date = date;

    }

public Score getScore(){ // get the corresponding game scores and filter them by comparing the player id
        return game.getScores().stream().filter(score -> score.getPlayer().getId().equals(player.getId())).findFirst().orElse(null);
};
    public void addSalvo(Salvo salvo){this.salvo.add(salvo); };

    public void addShip(Ship ship){this.ship.add(ship); };
//    public void addHit(String hit){this.hits.add(hit);};

//    public Set<String> getHits() {
//        return hits;
//    }

    public void addSetShips(Set<Ship> newShips){
        this.ship = newShips;
    }

    public Player getPlayer() { return player; }

    public Set<Ship> getShip() {
        return ship;
    }

    public Game getGame() {
        return game;
    }

    public Set<Salvo> getSalvoes() { return salvo; }

    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public Long getPlayerId(){
        return player.getId();
    }

//    public Set<Salvo> getSalvoForTurn(Integer turn){
//      return  this.salvo.stream().filter(s->s.getTurn().equals(turn)).collect(Collectors.toSet());
//return this.salvo.stream().findFirst().get().getTurn() == turn);
//        game.getGamePlayers().stream().findFirst().get().getPlayerId() == player.getId())
//                  .stream().filter(gp -> !gp.getId().equals(gamePlayer.getId())).findFirst().orElse(null);
//    };

    @Override
    public String toString() {
        return "GamePlayer{" +
                " id=" + id  +
                ", date=" + date +
                ", player=" + player +
                ", ship=" + ship +
                ", salvo=" + salvo +
                '}';

}
}
