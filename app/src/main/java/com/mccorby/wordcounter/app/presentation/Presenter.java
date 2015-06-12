package com.mccorby.wordcounter.app.presentation;

import com.mccorby.wordcounter.domain.entities.WordOccurrence;

/**
 * This interface must be implemented by any presenter in the system.
 * It forces those presenters to follow the lifecycle of an app (at least the main states)
 *
 * Created by JAC on 12/06/2015.
 */
public interface Presenter {

    void onCreate();
    void onResume();
    void onPause();
    void onDestroy();

}
