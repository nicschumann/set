package com.workshop.set.model.lang.exceptions;

import com.workshop.set.model.interfaces.Term;

/**
 * Created by nicschumann on 3/30/14.
 */
public class PatternMatchException extends Exception {
    public PatternMatchException(Term pattern, Term term ) {
        this.pattern = pattern;
        this.term = term;
    }

    private Term pattern;
    private Term term;
}
