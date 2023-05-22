package edu.eci.arsw.bombshowdown.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Syllables {

    private static Syllables instance;

    private Random random = new Random();

    List<String> syllablesList = Arrays.asList("au", "ar", "po", "mi", "az", "ama", "en", "di", "ara", "mos", "fe", "ai", "an", "gor", "me", "os", "se", "ili", "as", "ert", "de", "par", "na", "did", "rbi", "tan");

    ArrayList<String> syllables = new ArrayList<>(syllablesList);

    public ArrayList<String> getSyllables(){
        return syllables;
    }

    public String getRandomSyllable() {
        return syllables.get(random.nextInt(syllables.size()));
    }

    public static Syllables getInstance(){

        if(instance == null){
            instance = new Syllables();
        }

        return instance;
    }

}
