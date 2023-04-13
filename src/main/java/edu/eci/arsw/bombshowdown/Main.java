package edu.eci.arsw.bombshowdown;

import edu.eci.arsw.bombshowdown.entities.Player;
import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) throws IOException {

        /*BombShPersistenceImpl bs = new BombShPersistenceImpl();

        bs.addPlayer("andres");
        bs.addPlayer("julian");
        bs.addPlayer("juan pablo");
//        System.out.println(bs.getPlayers());

//        TEST ROTACION ENTRE VIVOS
//        bs.nextPlayer(); // julian
//
//        bs.nextPlayer(); // juan pablo
//
//        bs.nextPlayer(); // andres
//
//        bs.bombExplodes(); // -1 vida
//
//        bs.nextPlayer(); // julian
//
//        bs.nextPlayer(); // juan pablo
//
//        bs.nextPlayer(); // deberia ser julian pq andres murio
//
//        bs.setBombTimer();
//        bs.setSyllable();

<<<<<<< HEAD
        // funciona next player falta que no recorra los que no tienen vidas

        System.out.println(bs.getCurrentPlayer());
        System.out.println(bs);
        System.out.println(bs.checkWord("auditorio"));
        System.out.println(bs);*/
//        bs.nextPlayer();
//        System.out.println(bs.getCurrentPlayer().getName());
//        bs.nextPlayer();
//        System.out.println(bs.getCurrentPlayer().getName());
//        bs.nextPlayer();
//        System.out.println(bs.getCurrentPlayer().getName());
//        bs.nextPlayer();
//        System.out.println(bs.getCurrentPlayer().getName());
//        bs.nextPlayer();
//        System.out.println(bs.getCurrentPlayer().getName());
//        bs.nextPlayer();
//        System.out.println(bs.getCurrentPlayer().getName());


//        int t0 = (int) System.currentTimeMillis();
//        int t1 = t0 + bs.getBombTimer();
//
//        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
//        System.out.println("Introduzca Palabra");
//
//        String word = myObj.nextLine();  // Read user input
//        System.out.println("word introduced is: " + word);  // Output user input
//
//        // Deberia checkear la palabra pero esta fallando algo no se el que
//
//        JLanguageTool langTool = new JLanguageTool(new Spanish());
//        List<RuleMatch> matches = langTool.check(word);
//        System.out.println(matches.isEmpty() + "the current syllable is: " + bs.getSyllable());
//
//
//        bs.play(word, t0, t1);
        // bs.play(System.currentTimeMillis());

        Map<String, String> test = new ConcurrentHashMap<>();
        test.put("1", "a");
        test.put("2", "b");
        test.put("3", "c");
        test.put("4", "d");
        for (Map.Entry<String, String> turn : test.entrySet()) {
            if(turn.getKey().contains("3")){
                System.out.println("Bonus winner: " + turn.getKey());
                boolean bw = true;
                test.clear();
                break;
            } else {
                test.remove(turn.getKey());
                System.out.println(test.toString());
                test.put("5", "e");
            }
        }
        System.out.println(test.toString());
    }
}
