package com.mccorby.wordcounter.app.domain;

import android.os.Handler;
import android.os.Looper;

import com.mccorby.wordcounter.domain.abstractions.Bus;

import de.greenrobot.event.EventBus;

/**
 * Created by JAC on 11/06/2015.
 */
public class BusImpl implements Bus {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private EventBus mEventBus;

    public BusImpl(EventBus eventBus) {
        this.mEventBus = eventBus;
    }

    @Override
    public void post(final Object object) {
        handler.post(new Runnable() {
            @Override public void run() {
                mEventBus.post(object);
            }
        });
    }

    @Override
    public void register(Object object) {
        mEventBus.register(object);
    }

    @Override
    public void unregister(Object object) {
        mEventBus.unregister(object);
    }}
