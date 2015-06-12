package com.mccorby.wordcounter.domain.interactors;

import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;

/**
 * Interactor representing the use case "Get the list of all Word Occurrences".
 *
 * Created by JAC on 11/06/2015.
 */
public class GetWordListInteractor implements Interactor {

    private final WordOccurrenceRepository mRepository;
    private final Bus mBus;

    public GetWordListInteractor(Bus bus, WordOccurrenceRepository repository) {
        this.mRepository = repository;
        mBus = bus;
    }

    @Override
    public void execute() {
        mBus.post(mRepository.getWordOccurrences());
    }
}
