package com.mccorby.wordcounter.app.presentation;

/**
 * Created by JAC on 12/06/2015.
 */
public interface MainView {

    void processStarted();
    void notifyNewDataIsAvailable();
    void processDone();
}
