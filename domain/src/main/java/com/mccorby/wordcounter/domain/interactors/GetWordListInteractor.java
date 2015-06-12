package com.mccorby.wordcounter.domain.interactors;

import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;

/**
 * Interactor representing the use case "Get the list of all Word Occurrences".
 * It actually starts the process to obtain the words.
 * TODO It might have been good to rename it to GenerateWordListInteractor.
 *
 * Created by JAC on 11/06/2015.
 */
public class GetWordListInteractor implements Interactor {

    private final WordOccurrenceRepository mRepository;

    public GetWordListInteractor(WordOccurrenceRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public void execute() {
        mRepository.start();
    }
}
