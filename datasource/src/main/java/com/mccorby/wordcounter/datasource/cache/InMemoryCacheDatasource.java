package com.mccorby.wordcounter.datasource.cache;

import com.mccorby.wordcounter.datasource.entities.ProcessEvent;
import com.mccorby.wordcounter.datasource.entities.WordOccurrenceEvent;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;

import java.util.ArrayList;
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
 * Created by JAC on 11/06/2015.
 */
public class InMemoryCacheDatasource implements CacheDatasource {

    private Map<WordOccurrence, WordOccurrence> mData;
    private Bus mBus;

    private List<WordOccurrence> mClientList;

    public InMemoryCacheDatasource(Bus bus) {
        mData = new LinkedHashMap<>();
        this.mBus = bus;
    }

    @Override
    public synchronized List<WordOccurrence> getWordOccurrences() {
        if (mClientList == null) {
            mClientList = new ArrayList<>();
        }
        mClientList.clear();
        mClientList.addAll(mData.values());
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
        mBus.post(new ProcessEvent(ProcessEvent.EVENTS.DONE));
    }

    @Override
    public void restart(){
        mData.clear();
    }
}
