package edu.eci.arsw.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
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

    Map<String, BombShPersistence> rooms = new ConcurrentHashMap<>();

    @MessageMapping("/rooms/{room}")
    public void messagesHandler(String word, @DestinationVariable String room) throws Exception {
        msgt.convertAndSend("/rooms/"+room, word);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/rooms/{room}/players", consumes = "text/html")
    public ResponseEntity<?> handlerPutResource(@PathVariable String room, @RequestBody String player) {
        if (rooms.containsKey(room)) {
            System.out.println(player);
            BombShPersistence game = rooms.get(room);
            game.addPlayer(player);
            System.out.println(game);
            msgt.convertAndSend("/rooms/"+room, game.getPlayers());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/rooms/{room}/word", consumes = "text/html")
    public ResponseEntity<?> handlerPostResource(@PathVariable String room, @RequestBody String word) {
        try {
            BombShPersistence game = rooms.get(room);
            Gson gson = new Gson();
            JsonObject json = new JsonObject();
            json.addProperty("correct", game.checkWord(word.toLowerCase()));
            json.addProperty("syllable", game.getSyllable());
            json.addProperty("player", game.getCurrentPlayer().getName());
            System.out.println("/parties/"+room + gson.toJson(json));
            msgt.convertAndSend("/rooms/party/"+room, gson.toJson(json));
            return new ResponseEntity<>(gson.toJson(json), HttpStatus.OK);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(method = RequestMethod.POST, path = "/rooms/{room}")
    public ResponseEntity<?> handlerPostResourceRoom(@PathVariable String room) {
        if (!rooms.containsKey(room)) {
            rooms.put(room, new BombShPersistenceImpl());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }
}
