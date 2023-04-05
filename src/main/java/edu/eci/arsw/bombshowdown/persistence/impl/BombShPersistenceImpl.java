package edu.eci.arsw.bombshowdown.persistence.impl;

import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.entities.Syllables;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BombShPersistenceImpl implements BombShPersistence {

    private List<Player> players= new CopyOnWriteArrayList<>();

    private int currentPlayer = 0;

    private int bombTimer;

    private String currentSyllable;

    static Syllables syllablesInstance = Syllables.getInstance();

    private final ArrayList<String> syllables;

    public BombShPersistenceImpl(){
        syllables = syllablesInstance.getSyllables();
    }

    @Override
    public String getSyllable() {

        int max = syllables.size();
        int min = 0;
        int range = max - min + 1;
        int number = (int)(Math.random() * range) + min;

        currentSyllable = syllables.get(number);

        return syllables.get(number);

    }

    @Override
    public void deleteSyllable(String syllabe) {

    }

    @Override
    public boolean checkWord(String word) throws IOException {

        boolean flag = false;

        JLanguageTool langTool = new JLanguageTool(new Spanish());
        List<RuleMatch> matches = langTool.check(word);
        System.out.println(matches + "the current syllable is: " + currentSyllable);
        if(!matches.isEmpty() && word.contains(currentSyllable)){
            flag = true;
        }

        return flag;
    }

    @Override
    public List<Player> getPlayers() {
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

//        boolean alive = false;
//        int i = currentPlayer + 1;
//        int jumps = 0;
//
//        while (!alive){
//            if (players.get(i).getLives() == 0){
//                jumps += 1;
//            }
//            else alive = true;
//            i = (i + 1) % players.size();
//            currentPlayer = (currentPlayer + 1) % players.size();
//        }

        if(currentPlayer + 1  > players.size() - 1) currentPlayer = 0;
        else currentPlayer += 1;
        getCurrentPlayer();

    }

    @Override
    public void addPlayer(String name) {

//        for(Player player:players){
//            if(!players.isEmpty() & player.getName().equals(name)){
//                System.out.println("player name already exists, try another");
//            }
//            else {
//                Player newPlayer= new Player(name, 1);
//                players.add(newPlayer);
//            }
//        }

        Player newPlayer= new Player(name, 1);
        players.add(newPlayer);
    }

    @Override
    public void killPlayer() {

    }

    @Override
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    @Override
    public void updateLifes(String name) {
        for(Player player:players){
            if(player.getName().equals(name)){
                player.setLives(player.getLives());
            }
        }
    }

    @Override
    public void play(String word, int t0, int t1) throws IOException {
        boolean correct = false;
        int end = t0 + t1;
        while (!correct && System.currentTimeMillis() < end){
            if (checkWord(word)) {
                correct = true;
            }
        }
    }
}
