package com.mccorby.wordcounter.domain.abstractions;

import com.mccorby.wordcounter.domain.interactors.Interactor;

/**
 * The contract any interactor invoker in this system must comply to.
 *
 * Created by JAC on 11/06/2015.
 */
public interface InteractorInvoker {

    void execute(Interactor interactor);
}
