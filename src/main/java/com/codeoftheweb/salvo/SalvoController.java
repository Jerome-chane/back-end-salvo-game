package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    //////////////  ////////////// ////////////// ////////////// ////////////// ////////////// ////////////// ////////////// //////////////
    @Controller
    public class WebSocketController {
        @MessageMapping("/{game_id}")
        @SendTo("/topic/{game_id}")
    public Message update(@DestinationVariable Long game_id) throws Exception {
            return new Message("new update" );
        }

        @MessageMapping("/games")
        @SendTo("/topic/games")
        public Message updategame() throws Exception {
            return new Message("new update" );
        }
    }
    //////////////  ////////////// ////////////// ////////////// ////////////// ////////////// ////////////// ////////////// //////////////


    @RequestMapping(value = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvoes(@PathVariable Long gamePlayerId, @RequestBody String[] salvo, Authentication authentication) {
        GamePlayer gp = gamePlayerRepository.getOne(gamePlayerId);
        Player player = getAuthPlayer(authentication);
        Integer turn = (gp.getSalvoes().size() + 1);

        if(Status(gp).equals("Shoot !")) {

            if (isGuest(authentication) || gp == null || gamePlayerRepository.getOne(gamePlayerId).getPlayer().getId() != player.getId()) {
                return new ResponseEntity<>(makeMap("Error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
            }
            if (salvo.length == 0) {
                return new ResponseEntity<>(makeMap("Error", "There are no salvoes to place"), HttpStatus.UNAUTHORIZED);
            }
            if (gp.getSalvoes().stream().map(t -> t.getTurn()).equals(turn)) {
                return new ResponseEntity<>(makeMap("Forbidden", "You have already placed your salvoes at for this turn"), HttpStatus.FORBIDDEN);
            }
            Salvo newSalvo = new Salvo(turn, salvo, gp);
            salvoRepository.save(newSalvo);
            return new ResponseEntity<>(makeMap("Success", "Salvoes successfully added"), HttpStatus.CREATED);

    };
        return new ResponseEntity<>(makeMap("Error", "Unable to send salvoes"), HttpStatus.CREATED);
    };



    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(@PathVariable Long gamePlayerId, @RequestBody Set<Ship> ships, Authentication authentication) {
        GamePlayer gp = gamePlayerRepository.getOne(gamePlayerId);
        Player player = getAuthPlayer(authentication);


        if (isGuest(authentication) || gp == null || gamePlayerRepository.getOne(gamePlayerId).getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("Error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayerRepository.getOne(gamePlayerId).getShip().size() > 1) {
            return new ResponseEntity<>(makeMap("Error", "You already placed the ships"), HttpStatus.FORBIDDEN);
        }
        for (Ship ship : ships) {
            ship.setGamePlayer(gp);
            shipRepository.save(ship);
        }
        return new ResponseEntity<>(makeMap("Success", "Ships successfully added"), HttpStatus.CREATED);
    }


    @RequestMapping(value = "/api/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long nn, Authentication authentication) {
        Player player = getAuthPlayer(authentication);

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("Error", "You must be signed in to join a game"), HttpStatus.UNAUTHORIZED);
        } else {
            Game game = gameRepository.getOne(nn);
            if (game == null) {
                return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
            }
            if (game.getGamePlayers().size() >= 2) {
                return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);

            }
            if (game.getGamePlayers().size() == 1 && game.getGamePlayers().stream().findFirst().get().getPlayerId() == player.getId()) {
                return new ResponseEntity<>(makeMap("error", "Player already in the game"), HttpStatus.FORBIDDEN);

            } else {
                Date newDate = new Date();
                GamePlayer newGamePlayer = new GamePlayer(player, game, newDate);
                gamePlayerRepository.save(newGamePlayer);
                return new ResponseEntity<>(makeMap("gp_id", newGamePlayer.getId()), HttpStatus.CREATED);
            }
        }
    }

    @RequestMapping(value = "/api/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> newGame(Authentication authentication) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "You must be logged in to start a new game"), HttpStatus.UNAUTHORIZED);
        } else {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            Player player = getAuthPlayer(authentication);
            Date newDate = new Date();
            Game newGame = new Game(newDate);
            GamePlayer newGamePlayer = new GamePlayer(player, newGame, newDate);
            gameRepository.save(newGame);
            gamePlayerRepository.save(newGamePlayer);
//            System.out.println("New Game Created " + newGame);
//            System.out.println("New GP created " + newGamePlayer);
            dto.put("gp_id", newGamePlayer.getId());
            dto.put("game_id", newGame.getId());
            return new ResponseEntity<>(makeMap("new_game", dto), HttpStatus.ACCEPTED);

        }
    }

    @RequestMapping("/api/games")
    public Map<String, Object> getGames(Authentication authentication) {
//        System.out.println("request received");
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            dto.put("player", null);
        } else {
            dto.put("player", loginDTO(authentication));
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> GameDTO(game, authentication)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> loginDTO(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", playerRepository.findByEmail(authentication.getName()).getId());
        dto.put("firstname", playerRepository.findByEmail(authentication.getName()).getFirstName());
        dto.put("lastname", playerRepository.findByEmail(authentication.getName()).getLastName());
        dto.put("username", playerRepository.findByEmail(authentication.getName()).getUserName());

        return dto;
    }

    private Map<String, Object> GameDTO(Game game, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        if(!isGuest(authentication)){  // if player is logged in
            Player player = getAuthPlayer(authentication);
                Set<GamePlayer> gameGameplayers = game.getGamePlayers();
                Set<GamePlayer> playerGamePlayers = player.getGamePlayerSet();

                for (GamePlayer game_gp : gameGameplayers) {
                    for (GamePlayer player_gp : playerGamePlayers) {
                        if (game_gp.getId() == player_gp.getId()) {   // this gets the current player's gameplayer for this game
                            dto.put("status", Status(game_gp));   // this will retrun the current game status to the player
                        }
                    }
                }
//          if(player.getScore(game)!=null){

//          }

        }
        dto.put("game_id", game.getId());
        dto.put("created", game.getDate());
        dto.put("players_ids", game.getGamePlayers().stream().map(id -> id.getPlayerId()));
        dto.put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> makeGamePlayerDTO(gamePlayer)));
//        dto.put("all_players", game.getGamePlayers().stream().map(gamePlayer -> makePlayerDTO(gamePlayer.getPlayer())));
        return dto;
    }


    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gp_id", gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
//        dto.put("score", scoreDTO(gamePlayer.getScore()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player_id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("firstName", player.getFirstName());
        dto.put("lastName", player.getLastName());
        dto.put("email", player.getEmail());
        dto.put("all_results", player.getAllScores().stream().map(score -> score.getResult()));
        return dto;
    }

    private Map<String, Object> playerScoresDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("result", player.getAllScores());
        return dto;
    }

    private Map<String, Object> scoreDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
//        dto.put("player", score.getPlayerName());
        dto.put("result", score.getResult());
        return dto;
    }

    public Player getAuthPlayer(Authentication authentication) {
        return playerRepository.findByEmail(authentication.getName());
    }


    @RequestMapping("/api/game_view/{gpId}")
    public ResponseEntity<Map<String, Object>> gameView(@PathVariable Long gpId, Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gpId);
        Player player = gamePlayer.getPlayer();
        if (player != getAuthPlayer(authentication)) {
            return new ResponseEntity<>(makeMap("error", "You are not allowed to see this"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer != null) {
            dto.put("status", Status(gamePlayer));
            dto.put("game", GameDTO(gamePlayer.getGame(), authentication));
            dto.put("player", game_view_PlayerDTO(player));
            dto.put("ships", gamePlayer.getShip().stream().map(ship -> ShipDTO(ship)));
            dto.put("all_salvoes", gamePlayer.getSalvoes().stream().map(salvo -> salvo.getLocations()));
            dto.put("salvoes", gamePlayer.getSalvoes().stream().map(salvo -> SalvoDTO(salvo)));

            if(getOpponent(gamePlayer) != null){
                dto.put("opponent", game_view_Opponent_DTO(gamePlayer));
                dto.put("history", HistoryDTO(gamePlayer.getSalvoes(),gamePlayer));
            }
        }
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

    private String Status(GamePlayer gamePlayer){
        if(gamePlayer.getShip().size() == 0 ) { return "Place your Ships"; }
        if(gamePlayer.getGame().getGamePlayers().size() <  2){ return "Waiting for player to join..";}
        if(gamePlayer.getGame().getGamePlayers().size() == 2 ){
            if(gamePlayer.getShip().size() != 0){
                if(getOpponent(gamePlayer).getShip().size() == 0){ return "Waiting for Opponent to Place ships..";}
                if(getOpponent(gamePlayer).getShip().size() != 0){

                    if(gamePlayer.getSalvoes().size() == getOpponent(gamePlayer).getSalvoes().size()){
                       Integer player = 0;
                       Integer opponent = 0;
                    ///////////////////////////////////////////   ///////////////////////////////////////////   //////////////////////////////////////////////
                    List<Map<String, Object>> list = HistoryDTO(gamePlayer.getSalvoes(),gamePlayer);
                    for (Map<String, Object> map : list) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if(entry.getKey().contentEquals("all_sink")){
                                if( entry.getValue().toString().length()==53){
                                    player = 1;
                                    // Check if the current player is the winner
                                }
                            }
                        }
                    }
                    ///////////////////////////////////////////   ///////////////////////////////////////////   ///////////////////////////////////////////
                    ///////////////////////////////////////////   ///////////////////////////////////////////   //////////////////////////////////////////////
                    List<Map<String, Object>> list2 = HistoryDTO(getOpponent(gamePlayer).getSalvoes(),getOpponent(gamePlayer));
                    for (Map<String, Object> map : list2) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if(entry.getKey().contentEquals("all_sink")){
                                if(entry.getValue().toString().length()==53){
                                    opponent = 1;
                                    // Check if the Opponent is the winner
                                }
                            }
                        }
                    }
                        if(player == 1 && opponent == 1){
                            if(gamePlayer.getGame().getScores().size()<=1){
                                Score newScore = new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 0.5);
                                scoreRepository.save(newScore);
                            }
                            return "Draw!";};
                        if(player == 0 && opponent == 1){
                            if(gamePlayer.getGame().getScores().size()<=1){
                                Score newScore = new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 0);
                                scoreRepository.save(newScore);
                            }
                            return "You Lost!";}
                        if(player == 1 && opponent == 0){
                            if(gamePlayer.getGame().getScores().size()<=1){
                                Score newScore = new Score(gamePlayer.getPlayer(), gamePlayer.getGame(), 1);
                                scoreRepository.save(newScore);
                            }
                            return "Victory!";}
                    ///////////////////////////////////////////   ///////////////////////////////////////////   ///////////////////////////////////////////
                    }
                    if(gamePlayer.getSalvoes().size() > getOpponent(gamePlayer).getSalvoes().size()){ return "Waiting for opponent salvoes..";}
                    return "Shoot !";
                }
              }
           };
        return "";
    };



        private List<Map<String,Object>>HistoryDTO(Set<Salvo> salvoes, GamePlayer gamePlayer) {

        List<Salvo> salvoesSorted = salvoes.stream().collect(Collectors.toList());
        Collections.sort(salvoesSorted, (s1, s2) -> s1.getTurn().compareTo(s2.getTurn()));
        List<Map<String,Object>> history = new ArrayList<>();
        Set<String> allHits = new HashSet<>();
        Set<String> allSink = new HashSet<>();
        Set<Ship> opponentShips = new HashSet<>();
        for (Ship ship : getOpponent(gamePlayer).getShip()) {
            opponentShips.add(ship);
        }

        for (Salvo salvo : salvoesSorted) {
            Map<String, Object> dto = new LinkedHashMap<>();
            Set<String> hits = new HashSet<>();
            Set<String> miss = new HashSet<>();
            Set<String> sink = new HashSet<>();
            Integer turn = salvo.getTurn();
            String[] salvoLocations = salvo.getLocations();

            for (String shot : salvoLocations) {
                for (Ship ship : opponentShips) {
                    for (String singleLoc : ship.getLocations()) {
                        if (shot.equals(singleLoc)) {
                            hits.add(shot);
                            allHits.add(shot);
                            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            String[] allHitsArray = allHits.toArray(new String[allHits.size()]);
                            if (containsAll(ship.getLocations(), allHitsArray)) {
                                if(!allSink.contains(ship.getType())){
                                    sink.add(ship.getType());
                                };
                                allSink.add(ship.getType());
                            }
//                       //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                }
            }
                if (!hits.contains(shot)) {
                    miss.add(shot);
                }
            }
            dto.put("all_hits", allHits);
            dto.put("all_sink", allSink);
            dto.put("turn", turn);
            dto.put("salvoLocations", salvoLocations);
            dto.put("hits", hits);
            dto.put("missed", miss);
            dto.put("sink", sink);
            history.add(dto);

        }
        history.sort(Comparator.comparing(m->(Integer) m.get("turn"),Comparator.nullsLast(Comparator.naturalOrder())));
        return history;
    }

    private static boolean containsAll(String[] listA, String[] listB) {
        outer:
        for (String a : listA) {
            for (String b : listB) {
                if (a.equals(b)) {
                    continue outer;
                }
            }
            return false;
        }
        return true;
    }


    private Map<String, Object> ShipDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }
    private Map<String, Object> SalvoDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gp_id", salvo.getGamePlayerId());
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    // common methods :
    private GamePlayer getOpponent(GamePlayer gamePlayer) {
        return gamePlayer.getGame().getGamePlayers().stream().filter(gp -> !gp.getId().equals(gamePlayer.getId())).findFirst().orElse(null);

//        for (GamePlayer gp : gamePlayer.getGame().getGamePlayers()) {
//            if(gp.getId() != gamePlayer.getId()) {
//                return gp;
//            }
//        }
//
//   return null;    // Does the same as the line of code above

    }
    private Map<String, Object> game_view_Opponent_DTO(GamePlayer gamePlayer) {
        if(gamePlayer.getGame().getGamePlayers().size() < 2){
            return null;
        }

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
//        dto.put("firstName", getOpponent(gamePlayer).getPlayer().getFirstName());
//        dto.put("lastName", getOpponent(gamePlayer).getPlayer().getLastName());
        dto.put("userName", getOpponent(gamePlayer).getPlayer().getUserName());
        dto.put("opponent_id", getOpponent(gamePlayer).getPlayer().getId());
        dto.put("salvoes", getOpponent(gamePlayer).getSalvoes().stream().map(salvo -> SalvoDTO(salvo)));
        return dto;
    }
    private Map<String, Object> game_view_PlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player_id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("firstName", player.getFirstName());
        dto.put("lastName", player.getLastName());
        return dto;
    }


    @RequestMapping(value = "/api/signup", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addPlayer(@RequestBody Player player) {
//        public ResponseEntity<String> addPlayer(@RequestBody Player player) {
        Player p = playerRepository.findByEmail(player.getEmail());
        if (p != null) {
//            System.out.println("Player found" + p);
            return new ResponseEntity<>(makeMap("error","User already exists"),HttpStatus.CONFLICT);
//            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);

        }

        if (p == null) {
            Player newPlayer = new Player(player.getFirstName(), player.getLastName(), player.getUserName(), player.getEmail(), passwordEncoder.encode(player.getPassword()));
            playerRepository.save(newPlayer);
//            System.out.println("Player saved: " + newPlayer);

        }
         return new ResponseEntity<>(makeMap("success","Name Added"),HttpStatus.CREATED);
//        return new ResponseEntity<>("Name Added", HttpStatus.CREATED);
//        System.out.println(playerRepository.findAll());
    }
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}





