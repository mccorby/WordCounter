package com.mccorby.wordcounter.repository.test;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.repository.WordOccurrenceRepositoryImpl;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;

/**
 * Created by JAC on 14/06/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class RepositoryTest {

    @Mock
    private ExternalDatasource externalDatasource;
    @Mock
    private CacheDatasource cacheDatasource;
    @Mock
    Comparator<WordOccurrence> comparator;


    @Test
    public void testStart() {
        WordOccurrenceRepositoryImpl repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
        repo.start();
        Mockito.verify(externalDatasource).startReadingWords(repo);
    }

    @Test
    public void testAddWord() {
        WordOccurrenceRepositoryImpl repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
        WordOccurrence wo = new WordOccurrence("Test");
        repo.addWordOccurrence(wo);
        Mockito.verify(cacheDatasource).addWord(wo);
    }

    @Test
    public void testSorting() {
        WordOccurrenceRepositoryImpl repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
        repo.sort(comparator);
        Mockito.verify(cacheDatasource).sort(comparator);
    }

    @Test
    public void testResetSorting() {
        WordOccurrenceRepositoryImpl repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
        repo.resetSorting();
        Mockito.verify(cacheDatasource).resetSorting();
    }
}
