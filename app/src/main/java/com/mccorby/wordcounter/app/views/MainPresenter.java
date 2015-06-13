package com.mccorby.wordcounter.app.views;

import android.util.Log;

import com.mccorby.wordcounter.app.domain.interactors.SortWordOccurrencesInteractor;
import com.mccorby.wordcounter.app.presentation.MainView;
import com.mccorby.wordcounter.app.presentation.Presenter;
import com.mccorby.wordcounter.datasource.cache.InMemoryCacheDatasource;
import com.mccorby.wordcounter.datasource.entities.ProcessEvent;
import com.mccorby.wordcounter.datasource.entities.WordOccurrenceEvent;
import com.mccorby.wordcounter.datasource.file.FileDatasourceImpl;
import com.mccorby.wordcounter.datasource.network.NetworkDatasourceImpl;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.abstractions.InteractorInvoker;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.interactors.GetWordListInteractor;
import com.mccorby.wordcounter.domain.interactors.Interactor;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;
import com.mccorby.wordcounter.repository.WordOccurrenceRepositoryImpl;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * The presenter in the MVP pattern.
 * The presenter acts as a mediator between the view and the data.
 * The data is provided by a repository object.
 * Note: The lifecycle of the presenter differs from that of its view. In this case the Presenter is
 * a static reference in the view (fragment or activity) that is kept during recreation of the activity
 * (and its fragment). If a process or out of memory error happens, this object is likely to be killed
 * and recreated again in the view that invokes.
 * This has been done to avoid new calls to obtain the words if it's already in progress.
 * A side effect of this approach is that the presenter is never unregistered from the bus which might
 * cause problems in more complex applications. A solution would be the use of RxJava that offers
 * a better decoupling than a bus.
 * Created by JAC on 12/06/2015.
 */
public class MainPresenter implements Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();


    public enum SORTING {
        DEFAULT, ALPHANUMERIC, OCCURRENCES
    }

    Bus mBus;
    private WordOccurrenceRepository repo;
    private Interactor mInteractor;
    private InteractorInvoker mInteractorInvoker;

    private MainView mMainView;
    // Root directory for files stored locally
    private final File mRootDirectory;

    /**
     * A reference to the list of words to handle sorting.
     * I consider the sorted list belongs to the presentation layer thus it should
     * be handled directly by the presenter.
     */
    private List<WordOccurrence> mSortedList;

    public MainPresenter(MainView mainView, Bus bus, WordOccurrenceRepository repo,
                         Interactor defaultInteractor, InteractorInvoker interactorInvoker,
                         File rootDirectory) {
        this.mMainView = mainView;
        this.mRootDirectory = rootDirectory;
        this.mBus = bus;
        this.repo = repo;
        this.mInteractor = defaultInteractor;
        this.mInteractorInvoker = interactorInvoker;
    }

    public void setMainView(MainView mainView) {
        this.mMainView = mainView;
    }

    public WordOccurrence getWordOccurrence(int position) {
        if (mSortedList != null) {
            return mSortedList.get(position);
        }
        return repo.getWordOccurrences().get(position);
    }

    public int getWordListSize() {
        // Repository size and sorted list must be the same. No need to check.
        return repo.size();
    }

    // Events this presenter listens to

    public void onEvent(WordOccurrenceEvent event) {
        if (mMainView != null) {
            mMainView.notifyNewDataIsAvailable();
        }
    }

    public void onEvent(ProcessEvent event) {
        Log.d(TAG, "PROCESS => " + event.getEventType());
        if (mMainView != null) {
            switch (event.getEventType()) {
                case ERROR:
                    mMainView.displayError(event);
                    break;
                case STARTED:
                    if (mMainView != null) {
                        mMainView.processStarted();
                    }
                    break;
                case DONE:
                    if (mMainView != null) {
                        mMainView.processDone();
                    }
                    break;
                case SORT_DONE:
                    if (mMainView != null) {
                        mMainView.notifyNewDataIsAvailable();
                        mMainView.processDone();
                    }
                    break;
            }
        }
    }

    private Comparator<WordOccurrence> mAlphanumericComparator = new Comparator<WordOccurrence>() {
        @Override
        public int compare(WordOccurrence lhs, WordOccurrence rhs) {
            return lhs.getWord().compareToIgnoreCase(rhs.getWord());
        }
    };

    private Comparator<WordOccurrence> mOccurrencesComparator = new Comparator<WordOccurrence>() {
        @Override
        public int compare(WordOccurrence lhs, WordOccurrence rhs) {
            return rhs.getOccurrences() - lhs.getOccurrences();
        }
    };

    /**
     * Sorts the list as defined in the assignemnt.
     *
     * @param sorting the sorting type
     */
    public void sortList(SORTING sorting) {
        Comparator<WordOccurrence> comparator = null;
        switch (sorting) {
            case ALPHANUMERIC:
                mSortedList = repo.getWordOccurrences();
                comparator = mAlphanumericComparator;
                break;
            case OCCURRENCES:
                mSortedList = repo.getWordOccurrences();
                comparator = mOccurrencesComparator;
                break;
            case DEFAULT:
                // Sorted list no longer needed. Rely on repo data structure.
                mSortedList = null;
                break;
        }
        if (mSortedList != null) {
            mMainView.processStarted();
            Interactor sortInteractor = new SortWordOccurrencesInteractor(mSortedList, mBus, comparator);
            mInteractorInvoker.execute(sortInteractor);
        }
    }

    public void processFile(File file) {
        ExternalDatasource externalDatasource = new FileDatasourceImpl(mBus, file);
        CacheDatasource cacheDatasource = new InMemoryCacheDatasource(mBus);

        repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
        mInteractor = new GetWordListInteractor(repo);
        mMainView.processStarted();
        mInteractorInvoker.execute(mInteractor);
    }

    public void processUrl(URL url) {
        if (url != null) {
            ExternalDatasource externalDatasource = new NetworkDatasourceImpl(mBus, url);
            CacheDatasource cacheDatasource = new InMemoryCacheDatasource(mBus);

            repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
            mInteractor = new GetWordListInteractor(repo);
            mMainView.processStarted();
            mInteractorInvoker.execute(mInteractor);
        }
    }


    // Lifecycle related implementation

    @Override
    public void onCreate() {
        mBus.register(this);
        if (mMainView != null) {
            mMainView.processStarted();
        }
        mInteractorInvoker.execute(mInteractor);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        mMainView = null;
    }
}
