package com.mccorby.wordcounter.repository.datasources;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;

/**
 * Created by JAC on 11/06/2015.
 */
public interface ExternalDatasource {

    interface OnWordAvailableListener {
        void onWordAvailable(WordOccurrence word);
        void onProcessDone();
    }

    void startReadingWords(OnWordAvailableListener listener);
}
