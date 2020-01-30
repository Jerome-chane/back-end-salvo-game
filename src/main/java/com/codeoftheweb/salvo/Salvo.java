package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")

    private Long id;
    private Integer turn;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @OrderColumn(name="location_id")
    private String[] locations;

    public Salvo(){}

    public Salvo(Integer turn, String[] locations, GamePlayer gamePlayer){
        this.locations = locations;
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        gamePlayer.addSalvo(this);
    }

public Long getGamePlayerId(){ return this.gamePlayer.getId();}

    public GamePlayer getGamePlayer() { return gamePlayer; }

    public String[] getLocations() { return locations; }

    public Integer getTurn() { return turn; }

    @Override
    public String toString() {
        return "Salvo{" +
                "id=" + id +
                ", turn=" + turn +
//                ", gamePlayer=" + gamePlayer +
                ", locations=" + Arrays.toString(locations) +
                '}';
    }
}
