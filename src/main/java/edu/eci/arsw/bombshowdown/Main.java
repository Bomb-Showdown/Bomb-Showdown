package edu.eci.arsw.bombshowdown;

import edu.eci.arsw.bombshowdown.persistence.impl.BombShPersistenceImpl;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        BombShPersistenceImpl bs = new BombShPersistenceImpl();

        bs.addPlayer("ernesto");
        bs.addPlayer("julian");
        bs.addPlayer("andres");
        bs.addPlayer("juan pablo");
        bs.addPlayer("kalo");

        bs.setBombTimer();

        // funciona next player falta que no recorra los que no tienen vidas

        System.out.println(bs.getCurrentPlayer());
        System.out.println(bs);
        System.out.println(bs.checkWord("auditorio"));
        System.out.println(bs);
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


    }
}
