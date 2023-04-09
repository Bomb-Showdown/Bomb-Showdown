package edu.eci.arsw.bombshowdown.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Syllables {

    private static Syllables instance;

    List<String> syllablesList = Arrays.asList("aud", "ar", "po", "mi");

    ArrayList<String> syllables = new ArrayList<>(syllablesList);

    public ArrayList<String> getSyllables(){
        return syllables;
    }

    public String getRandomSyllable() {
        Random random = new Random();
        return syllables.get(random.nextInt(syllables.size()));
    }

    public static Syllables getInstance(){

        if(instance == null){
            instance = new Syllables();
        }

        return instance;
    }

}
