package edu.eci.arsw.bombshowdown.entities;

public class Player {

    private int lives;
    private String name;


    public Player(String name, int lives){
        this.lives = lives;
        this.name = name;
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

    @Override
    public String toString() {
        return "Player{" +
                "lives=" + lives +
                ", name='" + name + '\'' +
                '}';
    }
}
