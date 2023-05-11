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
import java.util.Scanner;
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

    private long timeSinceLastTurn;

    private int deadCount = 0;

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

        bonusWinner = false;
        boolean flag = false;
        List<RuleMatch> matches = langTool.check(word);

        if(matches.isEmpty() && word.contains(currentSyllable)){
            flag = true;
            this.nextPlayer();
            this.setSyllable();
            this.timeSinceLastTurn = System.currentTimeMillis();
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
                this.timeSinceLastTurn = System.currentTimeMillis();
                this.setSyllable();
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

        getCurrentPlayer();

        boolean alive = false;
        int i = (currentPlayer + 1) % players.size();

//        if(i > players.size() - 1){
//            i = 0;
//        }

        while (!alive){
            if (players.get(i).getLives() <= 0){
                i += (i + 1) % players.size();
                System.out.println("bucle");
            }
            else alive = true;
        }

        currentPlayer = i;

        getCurrentPlayer();

        System.out.println("-----------------------");

    }

    @Override
    public void addPlayer(String name) {
        Player newPlayer= new Player(name, 2, players.size());
        players.add(newPlayer);
    }

    @Override
    public void killPlayer() {
        players.get(currentPlayer).reduceLive();
        if (players.get(currentPlayer).getLives() == 0) {
            this.deadCount++;
        }
        nextPlayer();
        this.setSyllable();
        this.timeSinceLastTurn = System.currentTimeMillis();

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
        System.out.println(players.get(currentPlayer));
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

        correct = false;
        bombExplodes();

    }

    @Override
    public long getTimeSinceLastTurn() {
        return timeSinceLastTurn;
    }

    @Override
    public void setTimeSinceLastTurn(long timeSinceLastTurn) {
        this.timeSinceLastTurn = timeSinceLastTurn;
    }

    @Override
    public int getDeadCount() {
        return deadCount;
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
