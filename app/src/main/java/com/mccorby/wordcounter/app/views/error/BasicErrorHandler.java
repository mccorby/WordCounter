package com.mccorby.wordcounter.app.views.error;

import android.content.Context;
import android.widget.Toast;

import com.mccorby.wordcounter.datasource.entities.BaseEvent;

/**
 * Created by JAC on 13/06/2015.
 */
public class BasicErrorHandler implements ErrorHandler {

    @Override
    public void showError(Context context, BaseEvent error) {
        Toast.makeText(context, error.getErrorMessage(), Toast.LENGTH_LONG).show();
    }
}
