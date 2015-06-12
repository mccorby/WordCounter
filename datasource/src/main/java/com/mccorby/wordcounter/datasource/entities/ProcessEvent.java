package com.mccorby.wordcounter.datasource.entities;

/**
 * Created by JAC on 12/06/2015.
 */
public class ProcessEvent extends BaseEvent {

    public enum EVENTS {
        STARTED, DONE
    }

    public ProcessEvent(EVENTS eventType) {
        this.mEventType = eventType;
    }

    private EVENTS mEventType;

    public EVENTS getEventType() {
        return mEventType;
    }
}
