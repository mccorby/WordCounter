package com.mccorby.wordcounter.app.views.parsers;

import com.mccorby.wordcounter.datasource.entities.WordParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JAC on 14/06/2015.
 */
public class SimpleWordParser implements WordParser {

    @Override
    public List<String> parse(String line) {
        Pattern pattern = Pattern.compile("\\w+");
        Matcher matcher = pattern.matcher(line);
        List<String> wordList = new ArrayList<>();
        while (matcher.find()) {
            wordList.add(matcher.group());
        }
        return wordList;
    }
}
