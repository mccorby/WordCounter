package com.mccorby.wordcounter.domain.interactors;

import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An interactor representing the use case of sorting. Sort strategy is provided by the client.
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
        // TODO Better to send a signal as the list object is the same holded by the client that invoked the interactor.
        mBus.post(mList);
    }
}
