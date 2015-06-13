package com.mccorby.wordcounter.app.domain.interactors;

import com.mccorby.wordcounter.datasource.entities.ProcessEvent;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.interactors.Interactor;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An interactor representing the use case of sorting. Sort strategy is provided by the client.
 * This command could have been defined in the domain layer. However, I consider sorting to be a
 * presentation use case not affecting the underlying data.
 *
 * Created by JAC on 13/06/2015.
 */
public class SortWordOccurrencesInteractor implements Interactor {

    private List<WordOccurrence> mList;
    private Bus mBus;
    private Comparator<WordOccurrence> mComparator;

    public SortWordOccurrencesInteractor(List<WordOccurrence> list, Bus bus, Comparator<WordOccurrence> comparator) {
        this.mList = list;
        this.mBus = bus;
        this.mComparator = comparator;
    }

    @Override
    public void execute() {
        Collections.sort(mList, mComparator);
        mBus.post(new ProcessEvent(ProcessEvent.EVENTS.SORT_DONE));
    }
}
