package edu.eci.arsw.bombshowdown.persistence;

import edu.eci.arsw.bombshowdown.entities.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface BombShPersistence {

    public String getSyllable();

    public void deleteSyllable(String syllabe);
    public void setSyllable();

    boolean checkWord(String word) throws IOException;

    public List<Player> getPlayers();

    public int getBombTimer();

    public void setBombTimer();

    public void bombExplodes();

    public void nextPlayer();

    public void addPlayer(String name);

    public void killPlayer();

    public Player getCurrentPlayer();

    public void updateLifes(String name);


    void play(String word, int t0, int t1) throws IOException;
}
