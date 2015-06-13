package com.mccorby.wordcounter.datasource.cache;

import com.mccorby.wordcounter.datasource.entities.ProcessEvent;
import com.mccorby.wordcounter.datasource.entities.WordOccurrenceEvent;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of the cache datasource.
 * It's this class that defines the data structure to hold non-repeated values
 * I have used a LinkedHashMap as:
 * - It does not allow repeated values
 * - It provides a get method (as opposite to LinkedHashSet) with constant time cost
 * - Each new add to the cache (let that be as a new word or as an increment in an existing one)
 * will trigger a post in the post for registered objects to handle it.
 *
 * The cache holds a second list with the values that is properly built when required.
 * The alternative was to recreate the list from the values in the map every time the client
 * asked for it. This is a costly operation. I have eventually chosen performance over memory consumption.
 * If the number of objects in the map is too big, nonetheless, it would require some other kind of cache
 * as a database.
 * There are around 1 million words in English... worst case would be 2 million objects
 * in memory. To analyze
 *
 * Created by JAC on 11/06/2015.
 */
public class InMemoryCacheDatasource implements CacheDatasource {

    private Map<WordOccurrence, WordOccurrence> mData;
    private Bus mBus;

    private List<WordOccurrence> mClientList;

    public InMemoryCacheDatasource(Bus bus) {
        mData = new LinkedHashMap<>();
        mClientList = new ArrayList<>();
        this.mBus = bus;
    }

    @Override
    public synchronized List<WordOccurrence> getWordOccurrences() {
        return mClientList;
    }

    @Override
    public synchronized void addWord(WordOccurrence newWord) {
        WordOccurrence cachedWord = mData.get(newWord);
        if (cachedWord != null) {
            cachedWord.incrementOccurrence();
        } else {
            cachedWord = newWord;
        }
        mData.put(newWord, cachedWord);
        mClientList.clear();
        mClientList.addAll(mData.values());

        if (mBus != null) {
            mBus.post(new WordOccurrenceEvent(cachedWord));
        }
    }

    @Override
    public int size() {
        return mData.size();
    }

    @Override
    public void onProcessDone() {
    }

    @Override
    public synchronized void sort(Comparator<WordOccurrence> comparator) {
        Collections.sort(mClientList, comparator);
        mBus.post(new ProcessEvent(ProcessEvent.EVENTS.SORT_DONE));
    }

    @Override
    public synchronized void resetSorting() {
        mClientList.clear();
        mClientList.addAll(mData.values());
        mBus.post(new ProcessEvent(ProcessEvent.EVENTS.SORT_DONE));
    }
}
