package com.mccorby.wordcounter.app.views.di;

import com.mccorby.wordcounter.app.ActivityScope;
import com.mccorby.wordcounter.app.views.MainPresenter;
import com.mccorby.wordcounter.app.views.WordOccurrenceListFragment;
import com.mccorby.wordcounter.domain.abstractions.Bus;

import java.io.File;

import dagger.Component;

/**
 * Created by JAC on 13/06/2015.
 */
@ActivityScope
@Component(
        modules = MainModule.class
)
public interface MainComponent {

    void inject(WordOccurrenceListFragment fragment);
    void inject(MainPresenter presenter);

    MainPresenter getMainPresenter();
    File getRootDirectory();
    Bus getBus();

}
