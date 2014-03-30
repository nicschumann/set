package com.workshop.set.lang.exceptions;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Term;

/**
 * Created by nicschumann on 3/30/14.
 */
public class TypecheckingException extends Exception {
    public TypecheckingException( Term term, Context context ) {
        this.term = term;
        this.context = context;
    }

    public TypecheckingException( Term term, Context context, String s ) {
        this.term = term;
        this.context = context;
        this.text = s;
    }

    private final Term term;
    private final Context context;
    private String text = "";

}
