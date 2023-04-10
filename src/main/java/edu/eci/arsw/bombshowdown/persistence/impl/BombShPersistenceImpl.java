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
import java.util.Scanner;
import java.util.Timer;
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
        currentSyllable = syllables.get(0);
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
    public String getSyllable() {
        return currentSyllable;
    }

    @Override
    public boolean checkWord(String word) throws IOException {

        boolean flag = false;

        JLanguageTool langTool = new JLanguageTool(new Spanish());
        List<RuleMatch> matches = langTool.check(word);

        if(matches.isEmpty() && word.contains(currentSyllable)){
            flag = true;
            this.nextPlayer();
            this.setSyllable();
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

        boolean alive = false;
        int i = currentPlayer + 1;

        while (!alive){
            if (players.get(i).getLives() == 0){
                i += 1;
            }
            else alive = true;
        }

        currentPlayer = i;

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
    public void updateLives(String name) {
        for(Player player:players){
            if(player.getName().equals(name)){
                player.setLives(player.getLives());
            }
        }
    }

    @Override
    public void play(long t0) throws IOException {

        boolean correct = false;

        long start = System.currentTimeMillis();

        long end = start + 10 * 1000;

        while (!correct && System.currentTimeMillis() < end) {

            System.out.println("La silaba actual es -> " + currentSyllable);

            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Introduzca Palabra");

            String word = myObj.nextLine();  // Read user input
            System.out.println("word introduced is: " + word);  // Output user input
            if (checkWord(word)) {
                correct = true;
                nextPlayer();
                System.out.println("Palabra correcta, turno siguiente jugador");
            }


        }

        bombExplodes();

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
