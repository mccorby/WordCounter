package com.mccorby.wordcounter.app.views.di;

import android.os.Environment;

import com.mccorby.wordcounter.BuildConfig;
import com.mccorby.wordcounter.app.ActivityScope;
import com.mccorby.wordcounter.app.domain.BusImpl;
import com.mccorby.wordcounter.app.domain.InteractorInvokerImpl;
import com.mccorby.wordcounter.app.presentation.MainView;
import com.mccorby.wordcounter.app.views.error.BasicErrorHandler;
import com.mccorby.wordcounter.app.views.error.ErrorHandler;
import com.mccorby.wordcounter.app.views.main.MainPresenter;
import com.mccorby.wordcounter.app.views.parsers.SimpleWordParser;
import com.mccorby.wordcounter.datasource.cache.InMemoryCacheDatasource;
import com.mccorby.wordcounter.datasource.entities.WordParser;
import com.mccorby.wordcounter.datasource.network.NetworkDatasourceImpl;
import com.mccorby.wordcounter.domain.abstractions.Bus;
import com.mccorby.wordcounter.domain.abstractions.InteractorInvoker;
import com.mccorby.wordcounter.domain.interactors.GetWordListInteractor;
import com.mccorby.wordcounter.domain.interactors.Interactor;
import com.mccorby.wordcounter.domain.repository.WordOccurrenceRepository;
import com.mccorby.wordcounter.repository.WordOccurrenceRepositoryImpl;
import com.mccorby.wordcounter.repository.datasources.CacheDatasource;
import com.mccorby.wordcounter.repository.datasources.ExternalDatasource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

/**
 * Class providing the dependencies for the main screen/process
 * Created by JAC on 13/06/2015.
 */
@Module
public class MainModule {
    private MainView mMainView;

    public MainModule(MainView mainView) {
        this.mMainView = mainView;
    }

    @Provides
    @ActivityScope
    public MainPresenter provideMainPresenter(Bus bus, WordOccurrenceRepository repo,
                                              Interactor defaultInteractor,
                                              InteractorInvoker interactorInvoker, File root) {
        return new MainPresenter(mMainView, bus, repo, defaultInteractor, interactorInvoker, root);
    }

    @Provides
    public File provideRootDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    @Provides
    @ActivityScope
    public Bus provideBus() {
        return new BusImpl(new EventBus());
    }

    @Provides
    public WordParser provideWordParser() {
        return new SimpleWordParser();
    }

    @Provides
    @ActivityScope
    public CacheDatasource provideCacheDatasource(Bus bus, WordParser wordParser) {
        return new InMemoryCacheDatasource(bus, wordParser);
    }

    @Provides
    public URL provideURL() {
        URL url = null;
        try {
            url = new URL(BuildConfig.DEFAULT_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Provides
    @ActivityScope
    public ExternalDatasource provideExternalDatasource(Bus bus, URL url, WordParser parser) {
        return new NetworkDatasourceImpl(bus, parser, url);
    }

    @Provides
    @ActivityScope
    public WordOccurrenceRepository provideRepository(ExternalDatasource externalDatasource, CacheDatasource cacheDatasource) {
        return new WordOccurrenceRepositoryImpl(externalDatasource, cacheDatasource);
    }

    @Provides
    @ActivityScope
    public Interactor provideDefaultInteractor(WordOccurrenceRepository repository) {
        return new GetWordListInteractor(repository);
    }

    @Provides
    @ActivityScope
    public InteractorInvoker provideInteractorInvoker() {
        return new InteractorInvokerImpl(Executors.newSingleThreadExecutor());
    }

    @Provides
    @ActivityScope
    // TODO Should inject the Context here.
    public ErrorHandler provideErrorHandler() {
        return new BasicErrorHandler();
    }
}
