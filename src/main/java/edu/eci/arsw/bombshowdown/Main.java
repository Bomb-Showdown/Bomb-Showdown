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

        // bs.play(System.currentTimeMillis());


    }
}
