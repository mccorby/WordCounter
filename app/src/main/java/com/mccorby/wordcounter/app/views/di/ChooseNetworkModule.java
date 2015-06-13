package com.mccorby.wordcounter.app.views.di;

import com.mccorby.wordcounter.app.views.error.BasicErrorHandler;
import com.mccorby.wordcounter.app.views.error.ErrorHandler;

import dagger.Module;
import dagger.Provides;

/**
 * Created by JAC on 13/06/2015.
 */
@Module
public class ChooseNetworkModule {

    @Provides
    public ErrorHandler provideErrorHandler() {
        return new BasicErrorHandler();
    }
}
