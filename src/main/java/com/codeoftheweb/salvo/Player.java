package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayerSet = new HashSet<>();

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();


    public Player() { }

    public Player(String first, String last, String userName, String email, String password) {
        this.firstName = first;
        this.lastName = last;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }



    public void setPassword(String password) { this.password = password; }
    public String getPassword() { return password; }

    public Set<Score> getAllScores() { return scores;}
    public Score getScore(Game game){ return this.scores.stream().filter(score -> score.getGame().getId().equals(game.getId())).findFirst().orElse(null);}
    public void addScore(Score score) { this.scores.add(score); }
    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayerSet = gamePlayerSet;
    }
    public Set<GamePlayer> getGamePlayerSet() { return gamePlayerSet; }

    public void addGamePlayer(GamePlayer gamePlayer){ this.gamePlayerSet.add(gamePlayer); }


    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUserName() {
        return userName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setEmail(String email) {this.email = email; }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}