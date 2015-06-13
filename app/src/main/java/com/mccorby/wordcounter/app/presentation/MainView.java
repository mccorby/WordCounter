package com.mccorby.wordcounter.app.presentation;

import com.mccorby.wordcounter.datasource.entities.BaseEvent;

/**
 * Created by JAC on 12/06/2015.
 */
public interface MainView {

    void processStarted();
    void notifyNewDataIsAvailable();
    void processDone();

    void displayError(BaseEvent message);
}
