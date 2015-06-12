package com.mccorby.wordcounter.app.views;

import android.util.Log;

import com.mccorby.wordcounter.app.domain.BusImpl;
import com.mccorby.wordcounter.app.domain.InteractorInvokerImpl;
import com.mccorby.wordcounter.app.presentation.MainView;
import com.mccorby.wordcounter.app.presentation.Presenter;
import com.mccorby.wordcounter.datasource.cache.InMemoryCacheDatasource;
import com.mccorby.wordcounter.datasource.entities.ProcessEvent;
import com.mccorby.wordcounter.datasource.entities.WordOccurrenceEvent;
import com.mccorby.wordcounter.datasource.network.NetworkDatasourceImpl;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.interactors.GetWordListInteractor;
import com.mccorby.wordcounter.domain.interactors.Interactor;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;
import com.mccorby.wordcounter.repository.WordOccurrenceRepositoryImpl;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

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
    private Bus mBus;
    private WordOccurrenceRepository repo;
    private MainView mMainView;
    private Interactor mInteractor;
    private InteractorInvokerImpl mInteractorInvoker;

    public MainPresenter(MainView mainView) {
        this.mMainView = mainView;
        injectObjects();
    }

    public void setMainView(MainView mainView) {
        this.mMainView = mainView;
    }

    private void injectObjects() {
        // TODO For testing purposes. Remove
        EventBus eventBus = new EventBus();
        mBus = new BusImpl(eventBus);
        CacheDatasource cacheDatasource = new InMemoryCacheDatasource(mBus);


        URL url = null;
        try {
            url = new URL("http://textfiles.com/stories/3lpigs.txt");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ExternalDatasource externalDatasource = new NetworkDatasourceImpl(url);
        repo = new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);

        mInteractor = new GetWordListInteractor(repo);
        // The Executor is a SingleThreadExecutor that provides all I need in this assigment:
        // It has a single worker thread that executes tasks sequentially (actually just one task)
        mInteractorInvoker = new InteractorInvokerImpl(Executors.newSingleThreadExecutor());
    }

    public WordOccurrence getWordOccurrence(int position) {
        return repo.getWordOccurrences().get(position);
    }

    public int getWordListSize() {
        return repo.size();
    }

    // Events this presenter listens to

    public void onEvent(WordOccurrenceEvent event) {
        Log.d(TAG, "Received new word " + event.getWordOccurrence());
        if (mMainView != null) {
            mMainView.notifyNewDataIsAvailable();
        }

    }

    public void onEvent(ProcessEvent event) {
        Log.d(TAG, "PROCESS => " + event.getEventType());
        if (mMainView != null) {
            switch (event.getEventType()) {
                case STARTED:
                    break;
                case DONE:
                    mMainView.processDone();
                    break;
            }
        }
    }


    // Lifecycle related implementation

    @Override
    public void onCreate() {
        mBus.register(this);
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
