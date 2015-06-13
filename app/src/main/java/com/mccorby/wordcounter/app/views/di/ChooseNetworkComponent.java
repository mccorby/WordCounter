package com.mccorby.wordcounter.app.views.di;

import com.mccorby.wordcounter.app.ActivityScope;
import com.mccorby.wordcounter.app.views.chooser.ChooseNetworkActivity;

import dagger.Component;

/**
 * Created by JAC on 13/06/2015.
 */
@ActivityScope
@Component (
        modules = {ChooseNetworkModule.class}
)
public interface ChooseNetworkComponent {

    void inject(ChooseNetworkActivity activity);

}
