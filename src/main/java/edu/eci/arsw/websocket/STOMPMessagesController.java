package edu.eci.arsw.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
import edu.eci.arsw.bombshowdown.repository.LobbyPersistence;
import edu.eci.arsw.bombshowdown.repository.impl.RedisLobbyPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping(value = "/games")
public class STOMPMessagesController {

    @Autowired
    SimpMessagingTemplate msgt;

//    Map<String, BombShPersistence> rooms = new ConcurrentHashMap<>();
    LobbyPersistence rooms = new RedisLobbyPersistence();

    int bonusRounds = 0;

    @MessageMapping("/rooms/waiting-room/{room}")
    public void messagesHandler(String word, @DestinationVariable String room) throws Exception {
        msgt.convertAndSend("/rooms/waiting-room/"+room, word);
    }

    @MessageMapping("/rooms/text/{room}/player/{player}")
    public void textHandler(String word, @DestinationVariable String room, @DestinationVariable String player) throws Exception {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        BombShPersistence game = rooms.get(room);
        json.addProperty("player", game.find(player).getId());
        json.addProperty("word", word);
        msgt.convertAndSend("/rooms/text/"+room, gson.toJson(json));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/rooms/{room}/players", consumes = "text/html")
    public ResponseEntity<?> handlerPutResource(@PathVariable String room, @RequestBody String player) throws JsonProcessingException {
        BombShPersistence game = rooms.get(room);
        if (game != null) {
            System.out.println(player);
//            BombShPersistence game = rooms.get(room);
            game.addPlayer(player);
            System.out.println(game);
            msgt.convertAndSend("/rooms/waiting-room/"+room, game.getPlayers());
            rooms.save(room, game);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/rooms/{room}/word", consumes = "text/html")
    public ResponseEntity<?> handlerPostResource(@PathVariable String room, @RequestBody String word) {
        try {

            BombShPersistence game = rooms.get(room);
            System.out.println("Tiempo desde el último turno: " + (System.currentTimeMillis() - game.getTimeSinceLastTurn()));
            long time = (System.currentTimeMillis() - game.getTimeSinceLastTurn());
            Gson gson = new Gson();
            JsonObject json = new JsonObject();
            boolean res;
            if (time < 10000) {
                res = game.checkWord(word.toLowerCase());
                json.addProperty("correct", res);
            } else {
                json.addProperty("correct", "boom");
                json.addProperty("candidate", game.getCurrentPlayer().getId());
                game.killPlayer();
                res = false;
            } if (game.getDeadCount() == game.getPlayers().size()-1) {
                json.addProperty("winner", true);
                System.out.println("GAME OVER");
            }
            //boolean res = game.checkWord(word.toLowerCase());

            json.addProperty("syllable", game.getSyllable());
            json.addProperty("player", game.getCurrentPlayer().getName());
            System.out.println("/parties/"+room + gson.toJson(json));
            System.out.println(game);
            if (res) {
                bonusRounds++;
            }
            if (bonusRounds == 5) {
                bonusRounds = 0;
                // comienza ronda bonus
                json.addProperty("begin", true);
                //msgt.convertAndSend("/rooms/party/"+room, gson.toJson(json));
                msgt.convertAndSend("/rooms/bonus/"+room, gson.toJson(json));
            } else {
                msgt.convertAndSend("/rooms/party/"+room, gson.toJson(json));
            }
            System.out.println("van " + bonusRounds + " turnos");
            rooms.save(room, game);
            return new ResponseEntity<>(gson.toJson(json), HttpStatus.OK);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/rooms/{room}/players/{me}/bonus", consumes = "text/html")
    public ResponseEntity<?> handlerPostResourceBonus(@PathVariable String room, @PathVariable String me, @RequestBody String word) {
        try {
            BombShPersistence game = rooms.get(room);
            game.addQueue(me, word.toLowerCase());
            Gson gson = new Gson();
            JsonObject json = new JsonObject();
            boolean res = game.checkBonusWord();
            json.addProperty("correct", res);
            json.addProperty("syllable", game.getSyllable());
            json.addProperty("player", game.getCurrentPlayer().getName());
            json.addProperty("candidate", game.find(me).getId());
            json.addProperty("won", 1);
            System.out.println("/rooms/bonus/"+room + gson.toJson(json));
            msgt.convertAndSend("/rooms/bonus/"+room, gson.toJson(json));
            rooms.save(room, game);
            return new ResponseEntity<>(gson.toJson(json), HttpStatus.OK);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/rooms/{room}/start")
    public ResponseEntity<?> handlerStart(@PathVariable String room) throws JsonProcessingException {
        BombShPersistence game = rooms.get(room);
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        game.setTimeSinceLastTurn(System.currentTimeMillis());
        json.addProperty("correct", "");
        json.addProperty("syllable", game.getSyllable());
        json.addProperty("player", game.getCurrentPlayer().getName());
        System.out.println("/parties/"+room + gson.toJson(json));
        msgt.convertAndSend("/rooms/party/"+room, gson.toJson(json));
        rooms.save(room, game);
        return new ResponseEntity<>(gson.toJson(json), HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST, path = "/rooms/{room}")
    public ResponseEntity<?> handlerPostResourceRoom(@PathVariable String room) throws JsonProcessingException {
        BombShPersistence game = rooms.get(room);
        if (game == null) {
            rooms.save(room, new BombShPersistenceImpl());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }
}
