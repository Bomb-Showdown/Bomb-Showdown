package edu.eci.arsw.bombshowdown.entities;

public class Player {

    private int lives;
    private String name;
    private int id;


    public Player(String name, int lives, int id){
        this.lives = lives;
        this.name = name;
        this.id = id;
    }


    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void reduceLive(){
        this.lives = lives - 1;
    }
    public void addLive() {
        this.lives++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "lives=" + lives +
                ", name='" + name + '\'' +
                '}';
    }
}
