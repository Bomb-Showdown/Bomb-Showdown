package edu.eci.arsw.bombshowdown;

import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        long inicio = System.currentTimeMillis();
        JLanguageTool langTool = new JLanguageTool(new Spanish());
        List<RuleMatch> matches = langTool.check("casa");
        System.out.println(matches.isEmpty());
        System.out.println(System.currentTimeMillis() - inicio);

    }
}
