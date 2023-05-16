package edu.eci.arsw.bombshowdown;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
import edu.eci.arsw.bombshowdown.repository.impl.RedisLobbyPersistence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        Jedis jedis = new Jedis("3.91.61.183", 6379); // Establece la conexión a Redis
        String cachejson = jedis.get("1"); // Obtén el valor JSON asociado a la clave "1"

//        System.out.println(cachejson);

//        String json = "[{\"lives\": 2, \"name\": \"juan\", \"id\": 1}, {\"lives\": 2, \"name\": \"rodrigo\", \"id\": 2}]";
//
        ObjectMapper objectMapper = new ObjectMapper();
//        Player player = objectMapper.readValue(cachejson, Player.class);

//
//        List<Player> player = objectMapper.readValue(json, new TypeReference<List<Player>>() {});
//
//        System.out.println(player);
//
//
//        String json1 = "{\n" +
//                "    \"players\": [{\"lives\": 2, \"name\": \"juan\", \"id\": 1}, {\"lives\": 2, \"name\": \"rodrigo\", \"id\": 2}],\n" +
//                "    \"currentPlayer\": 0,\n" +
//                "    \"currentSyllable\": \"GE\",\n" +
//                "    \"started\": true,\n" +
//                "    \"bonusWinner\": false,\n" +
//                "    \"timeSinceLastTurn\": 2500,\n" +
//                "    \"deadCount\": 0\n" +
//                "}";
//

        RedisLobbyPersistence redisLobbyPersistence = new RedisLobbyPersistence();
//        BombShPersistence game = objectMapper.readValue(cachejson, BombShPersistenceImpl.class);
        BombShPersistence game = redisLobbyPersistence.get("2");
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        System.out.println(objectMapper.writeValueAsString(game));
//
        List<Player> players2 = new CopyOnWriteArrayList<>();
        players2.add(new Player("ernesto", 1, 1));

        BombShPersistence game2 = new BombShPersistenceImpl(players2, 0, "NA", false, false, 1200, 0);
        //redisLobbyPersistence.save("2", game2);

        System.out.println(game);
        System.out.println("{\"players\":[{\"lives\":2,\"name\":\"juan\",\"id\":1},{\"lives\":2,\"name\":\"rodrigo\",\"id\":2}],\"currentPlayer\":0,\"currentSyllable\":\"GE\",\"started\":true,\"bonusWinner\":false,\"timeSinceLastTurn\":2500,\"deadCount\":0}"
        );
        System.out.println(game.toJsonElement());
    }
}
