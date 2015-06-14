package com.mccorby.wordcounter.datasource.entities;

import java.util.List;

/**
 * Created by JAC on 14/06/2015.
 */
public interface WordParser {
    List<String> parse(String line);
}
