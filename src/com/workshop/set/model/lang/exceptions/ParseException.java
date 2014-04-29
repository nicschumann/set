package com.workshop.set.model.lang.exceptions;

import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.parser.Grammar.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nicschumann on 4/21/14.
 */
public class ParseException extends Exception {
    public ParseException( List<String> logs ) {
        this.log = logs;
    }

    public ParseException( Collection<Term> partialTerms, Collection<TERMINAL> parsed, Collection<TERMINAL> remaining ) {
        this.log = new LinkedList<>(Arrays.asList( "ParseError: Partially Constructed Derivation: " + partialTerms + ", parser position: " + parsed + " $ " + remaining ) );
    }

    private List<String> log;

    public List<String> getLog() {
        return log;
    }

    public void updateLog( String... errors ) {
        log.addAll( Arrays.asList( errors ) );
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return log.toString();
    }
}
