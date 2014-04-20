package com.workshop.set.model.lang.exceptions;

import com.workshop.set.model.interfaces.Context;
import com.workshop.set.model.interfaces.Term;

/**
 * Created by nicschumann on 3/30/14.
 */
public class TypecheckingException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1237094502089035017L;

	public TypecheckingException( Term term, Context<?> context ) {
        this.term = term;
        this.context = context;
    }

    public TypecheckingException( Term term, Context<?> context, String s ) {
        this.term = term;
        this.context = context;
        this.text = s;
    }

    private final Term term;
    private final Context<?> context;
    private String text = "";

    @Override
    public String getLocalizedMessage() {
        StringBuilder s = new StringBuilder("Typechecking Failed : " + text + System.lineSeparator() );
                      s.append( "\ton Term:\t" + term + "\n" );
                      s.append( "\tin Context:\t" + context + "\n" );

        return s.toString();
    }

}
