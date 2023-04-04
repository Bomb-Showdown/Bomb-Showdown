package edu.eci.arsw.bombshowdown.persistence;

import edu.eci.arsw.bombshowdown.entities.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface BombShPersistence {

    public Set<String> getSyllabes();

    public String getSyllabe();

    public void deleteSyllabe(String syllabe);

    public boolean checkWord(String word) throws IOException;

    public ArrayList<Player> getPlayers();

    public int getBombTimer();

    public void setBombTimer();

    public void bombExplodes();

    public void nextPlayer();

    public void addPlayer(String name);

    public void killPlayer();

    public Player getCurrentPlayer();

}
