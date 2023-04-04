package edu.eci.arsw.bombshowdown.persistence.impl;

import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.entities.Syllabes;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BombShPersistenceImpl implements BombShPersistence {

    private ArrayList<Player> players= new ArrayList<>();
    private int currentPlayer;
    JLanguageTool langTool = new JLanguageTool(new Spanish());
    private int bombTimer;

    static Syllabes syllabesInstance = Syllabes.getInstance();

    private final ArrayList<String> syllables;

    public BombShPersistenceImpl(){
        syllables = syllabesInstance.getSyllables();
    }



    @Override
    public Set<String> getSyllabes() {
        return null;
    }

    @Override
    public String getSyllabe() {

        int max = syllables.size();
        int min = 0;
        int range = max - min + 1;
        int number = (int)(Math.random() * range) + min;

        return syllables.get(number);

    }

    @Override
    public void deleteSyllabe(String syllabe) {

    }

    @Override
    public boolean checkWord(String word) throws IOException {

        boolean flag = false;

        List<RuleMatch> matches = langTool.check(word);
        if(!matches.isEmpty()){
            flag = true;
        }

        return flag;
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public int getBombTimer() {
        return bombTimer;
    }

    @Override
    public void setBombTimer() {
        int max = 10;
        int min = 1;
        int range = max - min + 1;
        bombTimer = (int)(Math.random() * range) + min;
    }

    @Override
    public void bombExplodes() {
        getCurrentPlayer().reduceLive();
    }

    @Override
    public void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }

    @Override
    public void addPlayer(String name) {

        for(Player player:players){
            if(player.getName().equals(name)){
                System.out.println("player name already exists, try another");
            }
            else {
                Player newPlayer= new Player(name, 3);
            players.add(newPlayer);
            }
        }

        Player newPlayer= new Player(name, 3);
        players.add(newPlayer);
    }

    @Override
    public void killPlayer() {

    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }
}
