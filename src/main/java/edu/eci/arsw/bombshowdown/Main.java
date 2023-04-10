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
        bs.setSyllable();


        System.out.println(bs.getCurrentPlayer());
        System.out.println(bs);
        System.out.println(bs.checkWord("auditorio"));
        System.out.println(bs);


    }
}
