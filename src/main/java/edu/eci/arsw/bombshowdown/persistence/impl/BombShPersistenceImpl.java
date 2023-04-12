package edu.eci.arsw.bombshowdown.persistence.impl;

import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.entities.Syllables;
import edu.eci.arsw.bombshowdown.entities.Tuple;
import edu.eci.arsw.bombshowdown.persistence.BombShPersistence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class BombShPersistenceImpl implements BombShPersistence {

    private List<Player> players= new CopyOnWriteArrayList<>();

    private int currentPlayer = 0;

    private int bombTimer;

    private String currentSyllable;

    static Syllables syllablesInstance = Syllables.getInstance();

    private final ArrayList<String> syllables;

    JLanguageTool langTool = new JLanguageTool(new Spanish());

    private boolean started = false;

    private boolean bonusWinner = false;

    private ConcurrentLinkedQueue<Tuple<String, String>> queue = new ConcurrentLinkedQueue<>();

    public BombShPersistenceImpl(){
        syllables = syllablesInstance.getSyllables();
        currentSyllable = syllables.get(0);
    }

    @Override
    public String getSyllable() {

//        int max = syllables.size();
//        int min = 0;
//        int range = max - min + 1;
//        int number = (int)(Math.random() * range) + min;
//
//        currentSyllable = syllables.get(number);
//
//        return syllables.get(number);
        return currentSyllable;

    }

    @Override
    public void deleteSyllable(String syllabe) {

    }

    @Override
    public void setSyllable() {
        currentSyllable = syllablesInstance.getRandomSyllable();
        //System.out.println(currentSyllable);
    }

    @Override
    public boolean checkWord(String word) throws IOException {

        bonusWinner = false;
        boolean flag = false;
        List<RuleMatch> matches = langTool.check(word);
        System.out.println(matches + "the current syllable is: " + currentSyllable);
        if(matches.isEmpty() && word.contains(currentSyllable)){
            flag = true;
            this.nextPlayer();
            this.setSyllable();
        }

        return flag;
    }

    @Override
    public boolean checkBonusWord() throws IOException {
        if (!bonusWinner) {
            Tuple<String, String> turn = queue.poll();
            System.out.println("Tupla: " + turn);
            List<RuleMatch> matches = langTool.check(turn.getElem2());
            System.out.println(matches + "the current syllable is: " + currentSyllable);
            if(matches.isEmpty() && turn.getElem2().contains(currentSyllable)){
                System.out.println("Bonus winner: " + turn.getElem1());
                bonusWinner = true;
                for (Player py : players) {
                    if (py.getName().equals(turn.getElem1())) {
                        py.addLive();
                    }
                }
                System.out.println(players);
                queue.clear();
            }
        }
        return bonusWinner;
    }

    @Override
    public void addQueue(String player, String word) {
        queue.add(new Tuple<>(player, word));
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
        if(currentPlayer + 1  > players.size() - 1) currentPlayer = 0;
        else currentPlayer += 1;
        //System.out.println(getCurrentPlayer());

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

        Player newPlayer= new Player(name, 1, players.size());
        players.add(newPlayer);
    }

    @Override
    public void killPlayer() {

    }


    @Override
    public Player find(String name) {
        for (Player py : players) {
            if (py.getName().equals(name)) {
                return py;
            }
        }
        return null;
    }

    @Override
    public void start() {
        started = true;
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

    @Override
    public String toString() {
        return "BombShPersistenceImpl{" +
                "players=" + players +
                ", currentPlayer=" + currentPlayer +
                ", bombTimer=" + bombTimer +
                ", currentSyllable='" + currentSyllable +
                ", syllables=" + syllables +
                '}';
    }
}
