package com.mccorby.wordcounter.app.views.di;

import com.mccorby.wordcounter.app.ActivityScope;
import com.mccorby.wordcounter.app.views.main.MainPresenter;
import com.mccorby.wordcounter.app.views.main.WordOccurrenceListFragment;

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
}
