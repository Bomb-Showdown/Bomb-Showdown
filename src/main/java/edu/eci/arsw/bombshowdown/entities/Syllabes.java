package edu.eci.arsw.bombshowdown.entities;

import io.vavr.collection.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Syllabes {

    private static Syllabes instance;

    List<String> syllablesList = Arrays.asList("a", "ab", "c", "d");

    ArrayList<String> syllables = new ArrayList<>(syllablesList);

    public ArrayList<String> getSyllables(){
        return syllables;
    }

    public static Syllabes getInstance(){

        if(instance == null){
            instance = new Syllabes();
        }

        return instance;
    }

}
