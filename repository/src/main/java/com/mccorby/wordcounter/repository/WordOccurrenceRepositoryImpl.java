package com.mccorby.wordcounter.repository;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import java.util.List;

/**
 * An implementation of the WordOccurrence repository as defined by the domain layer.
 * The repository starts the magic by handling the data processed by the datasources associated to it.
 * Acting as an observer of the external datasource, the repository can update the cache datasource
 * accordingly. This way I avoid coupling both datasources
 *
 * The repository delegates the retrieval of processed WordOccurrences to the CacheDatasource object
 */
public class WordOccurrenceRepositoryImpl implements WordOccurrenceRepository, ExternalDatasource.OnWordAvailableListener {

    private ExternalDatasource mExternalDatasource;
    private CacheDatasource mCacheDatasource;

    public WordOccurrenceRepositoryImpl(ExternalDatasource externalDatasource, CacheDatasource cacheDatasource) {
        this.mExternalDatasource = externalDatasource;
        this.mCacheDatasource = cacheDatasource;
    }

    @Override
    public void start() {
        mCacheDatasource.restart();
        mExternalDatasource.startReadingWords(this);
    }

    @Override
    public void addWordOccurrence(WordOccurrence wordOccurrence) {
        mCacheDatasource.addWord(wordOccurrence);
    }


    @Override
    public List<WordOccurrence> getWordOccurrences() {
        return mCacheDatasource.getWordOccurrences();
    }

    @Override
    public void onWordAvailable(WordOccurrence word) {
        mCacheDatasource.addWord(word);
    }

    @Override
    public int size() {
        return mCacheDatasource.size();
    }

    @Override
    public void onProcessDone() {
        mCacheDatasource.onProcessDone();
    }
}
