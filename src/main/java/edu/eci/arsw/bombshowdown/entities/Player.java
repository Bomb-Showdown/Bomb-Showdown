package edu.eci.arsw.bombshowdown.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {

    @JsonProperty("lives")
    private int lives;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private int id;


    public Player() {

    }

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
        return "{" +
                "\"lives\":" + lives +
                ", \"name\":\"" + name + '\"' +
                ", \"id\":" + id +
                '}';
    }
}
