package com.workshop.set.lang.exceptions;

import com.workshop.set.interfaces.Context;

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
