package com.mccorby.wordcounter.datasource.entities;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;

/**
 * Created by JAC on 12/06/2015.
 */
public class WordOccurrenceEvent extends BaseEvent {

    private WordOccurrence mWordOccurrence;

    public WordOccurrenceEvent(WordOccurrence wordOccurrence) {
        this.mWordOccurrence = wordOccurrence;
    }

    public WordOccurrence getWordOccurrence() {
        return mWordOccurrence;
    }
}
