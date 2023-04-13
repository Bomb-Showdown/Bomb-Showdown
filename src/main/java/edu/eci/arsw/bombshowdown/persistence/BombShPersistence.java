package edu.eci.arsw.bombshowdown.persistence;

import edu.eci.arsw.bombshowdown.entities.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface BombShPersistence {

    public void deleteSyllable(String syllabe);

    public void setSyllable();

    String getSyllable();

    boolean checkWord(String word) throws IOException;

    boolean checkBonusWord() throws IOException;

    public void addQueue(String player, String word);

    public List<Player> getPlayers();

    public int getBombTimer();

    public void setBombTimer();

    public void bombExplodes();

    public void nextPlayer();

    public void addPlayer(String name);

    public void killPlayer();
    public Player find(String name);

    public void start();

    public Player getCurrentPlayer();

    public void updateLives(String name);

    void play(long t0) throws IOException;
}
