package edu.eci.arsw.bombshowdown.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Syllables {

    private static Syllables instance;

    List<String> syllablesList = Arrays.asList("a", "ab", "c", "d");

    ArrayList<String> syllables = new ArrayList<>(syllablesList);

    public ArrayList<String> getSyllables(){
        return syllables;
    }


    public static Syllables getInstance(){

        if(instance == null){
            instance = new Syllables();
        }

        return instance;
    }

}
