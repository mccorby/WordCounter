package com.mccorby.wordcounter.datasource.entities;

/**
 * Base event used to notify via bus.
 * It has a very simple error include on it that can be used by the presentation layer to
 * display it.
 * Created by JAC on 12/06/2015.
 */
public class BaseEvent {

    private String mErrorMessage;


    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
