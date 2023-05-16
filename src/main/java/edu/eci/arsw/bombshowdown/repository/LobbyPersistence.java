package edu.eci.arsw.bombshowdown.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;

public interface LobbyPersistence {

    public void save(String id, BombShPersistence game);

    public BombShPersistence get(String id) throws JsonProcessingException;

}
