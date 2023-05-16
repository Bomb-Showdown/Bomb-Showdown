package edu.eci.arsw.bombshowdown.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
import edu.eci.arsw.bombshowdown.repository.LobbyPersistence;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class RedisLobbyPersistence implements LobbyPersistence {

    private static final String SERVER_IP = "3.91.61.183";
    private static final int REDIS_PORT = 6379;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void save(String id, BombShPersistence game) {
        Jedis jedis = new Jedis("3.91.61.183", 6379);
        String cache = jedis.set(id, game.toJsonElement());
        jedis.close();
    }

    @Override
    public BombShPersistence get(String id) throws JsonProcessingException {
        Jedis jedis = new Jedis("3.91.61.183", 6379);
        String cache = jedis.get(id);
        jedis.close();
        return objectMapper.readValue(cache, BombShPersistenceImpl.class);
    }
}
