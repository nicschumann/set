package com.workshop.set.model.lang.exceptions;

import com.workshop.set.model.interfaces.Context;

/**
 * Created by nicschumann on 4/2/14.
 */
public class ProofFailureException extends Exception {
    public ProofFailureException( String t ) {
        msg = t;
    }

    private String msg;

    @Override
    public String getLocalizedMessage() {
        return "ProofFailureException: " + msg;
    }
}
