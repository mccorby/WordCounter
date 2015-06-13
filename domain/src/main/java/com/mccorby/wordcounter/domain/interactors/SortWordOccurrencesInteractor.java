package com.mccorby.wordcounter.domain.interactors;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;

import java.util.Comparator;

/**
 * An interactor representing the use case of sorting. Sort strategy is provided by the client.
 * This command could have been defined in the domain layer.
 * Previously I thought it could be in the presentation layer... changed my mind as the underlying
 * repository could be anything from a database to memory.
 * <p/>
 * Created by JAC on 13/06/2015.
 */
public class SortWordOccurrencesInteractor implements Interactor {
    private Comparator<WordOccurrence> mComparator;

    private WordOccurrenceRepository mRepository;

    public SortWordOccurrencesInteractor(WordOccurrenceRepository repo,
                                         Comparator<WordOccurrence> comparator) {
        this.mComparator = comparator;
        this.mRepository = repo;
    }

    @Override
    public void execute() {
        if (mComparator != null) {
            mRepository.sort(mComparator);
        } else {
            mRepository.resetSorting();
        }

    }
}
