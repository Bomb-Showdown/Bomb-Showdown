package edu.eci.arsw.bombshowdown.persistence;

import edu.eci.arsw.bombshowdown.entities.Player;

import java.io.IOException;
import java.util.List;

public interface BombShPersistence {

    public void setSyllable();

    String getSyllable();

    boolean checkWord(String word) throws IOException;

    boolean checkBonusWord() throws IOException;

    public void addQueue(String player, String word);

    public List<Player> getPlayers();

    public int getBombTimer();

    public void bombExplodes();

    public void nextPlayer();

    public void addPlayer(String name);

    public void killPlayer();
    public Player find(String name);

    public void start();

    public Player getCurrentPlayer();

    public void updateLives(String name);

    public long getTimeSinceLastTurn();

    public void setTimeSinceLastTurn(long timeSinceLastTurn);

    public int getDeadCount();

    public String toJsonElement();

}
