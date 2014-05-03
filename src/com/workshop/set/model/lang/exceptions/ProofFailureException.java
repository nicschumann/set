package com.workshop.set.model.lang.exceptions;


/**
 * Created by nicschumann on 4/2/14.
 */
public class ProofFailureException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3363323403119840535L;

	public ProofFailureException( String t ) {
        msg = t;
    }

    private String msg;

    @Override
    public String getLocalizedMessage() {
        return msg;
    }
}
