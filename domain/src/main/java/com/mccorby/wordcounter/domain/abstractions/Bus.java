package com.mccorby.wordcounter.domain.abstractions;

/**
 * The contract a bus in this app must follow.
 * The system (the app actually but could be anything else on top of this) is built using
 * asynchronous communication between the layer. This is achieve by a bus.
 *
 * Created by JAC on 11/06/2015.
 */
public interface Bus {

    void register(Object object);
    void unregister(Object object);
    void post(Object event);
}
