package com.mccorby.wordcounter.app.views.error;

import android.content.Context;

import com.mccorby.wordcounter.datasource.entities.BaseEvent;

/**
 * Created by JAC on 13/06/2015.
 */
public interface ErrorHandler {

    void showError(Context context, BaseEvent error);
}
