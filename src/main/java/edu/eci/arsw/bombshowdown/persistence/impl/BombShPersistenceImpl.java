package edu.eci.arsw.bombshowdown.persistence.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import com.fasterxml.jackson.annotation.JsonProperty;

public class BombShPersistenceImpl implements BombShPersistence {

    @JsonProperty("players")
    private List<Player> players= new CopyOnWriteArrayList<>();

    @JsonProperty("currentPlayer")
    private int currentPlayer = 0;

    private int bombTimer;

    @JsonProperty("currentSyllable")
    private String currentSyllable;

    static Syllables syllablesInstance = Syllables.getInstance();

    private final ArrayList<String> syllables;

    JLanguageTool langTool = new JLanguageTool(new Spanish());

    @JsonProperty("started")
    private boolean started = false;

    @JsonProperty("bonusWinner")
    private boolean bonusWinner = false;

    private ConcurrentLinkedQueue<Tuple<String, String>> queue = new ConcurrentLinkedQueue<>();

    @JsonProperty("timeSinceLastTurn")
    private long timeSinceLastTurn;

    @JsonProperty("deadCount")
    private int deadCount = 0;

    public BombShPersistenceImpl(){
        syllables = syllablesInstance.getSyllables();
        currentSyllable = syllables.get(0);
    }

    public BombShPersistenceImpl(List<Player> players, int currentPlayer, String currentSyllable, boolean started, boolean bonusWinner, long timeSinceLastTurn, int deadCount){
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.currentSyllable = currentSyllable;
        this.started = started;
        this.bonusWinner = bonusWinner;
        this.timeSinceLastTurn = timeSinceLastTurn;
        this.deadCount = deadCount;

        syllables = syllablesInstance.getSyllables();
        langTool = new JLanguageTool(new Spanish());
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
                i = (i + 1) % players.size();
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

    public String toJsonElement() {
        Gson gson = new Gson();
        //{"players":[{"lives":2,"name":"juan","id":1},{"lives":2,"name":"rodrigo","id":2}],"currentPlayer":0,"currentSyllable":"GE","started":true,"bonusWinner":false,"timeSinceLastTurn":2500,"deadCount":0}
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (Player pl: players) {
            jsonArray.add(gson.toJsonTree(pl));
        }
        jsonObject.add("players", jsonArray);
        jsonObject.addProperty("currentPlayer", currentPlayer);
        jsonObject.addProperty("currentSyllable", currentSyllable);
        jsonObject.addProperty("started", started);
        jsonObject.addProperty("bonusWinner", bonusWinner);
        jsonObject.addProperty("timeSinceLastTurn", timeSinceLastTurn);
        jsonObject.addProperty("deadCount", deadCount);
        return gson.toJson(jsonObject);
    }

    @Override
    public String toString() {
        return "BombShPersistenceImpl{" +
                "players=" + players +
                ", currentPlayer=" + currentPlayer +
                ", currentSyllable='" + currentSyllable + '\'' +
                ", started=" + started +
                ", bonusWinner=" + bonusWinner +
                ", timeSinceLastTurn=" + timeSinceLastTurn +
                ", deadCount=" + deadCount +
                '}';
    }
}
