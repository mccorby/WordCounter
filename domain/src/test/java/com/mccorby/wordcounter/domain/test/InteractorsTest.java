package com.mccorby.wordcounter.domain.test;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.interactors.GetWordListInteractor;
import com.mccorby.wordcounter.domain.interactors.Interactor;
import com.mccorby.wordcounter.domain.interactors.SortWordOccurrencesInteractor;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by JAC on 14/06/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class InteractorsTest {

    @Mock
    WordOccurrenceRepository repository;

    @Mock
    Comparator<WordOccurrence> comparator;

    @Test
    public void testWordListInteractor() {
        assertNotNull(repository);
        Interactor interactor = new GetWordListInteractor(repository);
        interactor.execute();
        Mockito.verify(repository).start();
    }

    @Test
    public void testSortWordInteractor() {
        assertNotNull(repository);
        Interactor interactor = new SortWordOccurrencesInteractor(repository, comparator);
        interactor.execute();
        Mockito.verify(repository).sort(comparator);
    }
}
