package com.mccorby.wordcounter.repository.datasources;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;

import java.util.List;

/**
 * A caching datasource definition.
 * Classes implementing this interface will be the ones used by the upper layers to display
 * the word occurrences.
 * Such classes will hold the concrete data structure used to answer the business model.
 *
 * Created by JAC on 11/06/2015.
 */
public interface CacheDatasource {
    List<WordOccurrence> getWordOccurrences();
    void addWord(WordOccurrence newWord);
    int size();
    void onProcessDone();
    void restart();
}
