package com.mccorby.wordcounter.app.views;

import android.util.Log;

import com.mccorby.wordcounter.app.domain.BusImpl;
import com.mccorby.wordcounter.app.presentation.MainView;
import com.mccorby.wordcounter.app.presentation.Presenter;
import com.mccorby.wordcounter.datasource.cache.InMemoryCacheDatasource;
import com.mccorby.wordcounter.datasource.entities.ProcessEvent;
import com.mccorby.wordcounter.datasource.entities.WordOccurrenceEvent;
import com.mccorby.wordcounter.datasource.network.NetworkDatasourceImpl;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.entities.WordOccurrence;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;
import com.mccorby.wordcounter.repository.WordOccurrenceRepositoryImpl;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import java.net.MalformedURLException;
import java.net.URL;

import de.greenrobot.event.EventBus;

/**
 * Created by JAC on 12/06/2015.
 */
public class MainPresenter implements Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();
    private Bus mBus;
    private WordOccurrenceRepository repo;
    private MainView mView;

    public MainPresenter(MainView view) {
        this.mView = view;
        injectObjects();
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
        mView.notifyNewDataIsAvailable();

    }

    public void onEvent(ProcessEvent event) {
        Log.d(TAG, "PROCESS => " + event.getEventType());
        switch (event.getEventType()) {
            case STARTED:
                break;
            case DONE:
                mView.processDone();
                break;
        }

    }


    // Lifecycle related implementation

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {
        mBus.register(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                repo.start();
            }
        }).start();
    }

    @Override
    public void onPause() {
        mBus.unregister(this);
    }

    @Override
    public void onDestroy() {

    }
}
