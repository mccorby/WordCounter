package com.mccorby.wordcounter.domain.repository;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;

import java.util.Collection;
import java.util.List;

/**
 * The contract a repository must comply to to deal with the domain entities.
 * The repository defines the way data is handled.
 *
 * Created by JAC on 11/06/2015.
 */
public interface WordOccurrenceRepository {

    void start();
    int size();

    void addWordOccurrence(WordOccurrence wordOccurrence);
    List<WordOccurrence> getWordOccurrences();
}
