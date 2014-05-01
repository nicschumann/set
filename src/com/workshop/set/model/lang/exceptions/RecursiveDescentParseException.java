package com.workshop.set.model.lang.exceptions;

import com.workshop.set.model.lang.parser.Grammar;

import java.util.List;

/**
 * Created by nicschumann on 4/22/14.
 */
public class RecursiveDescentParseException extends Exception {
    public RecursiveDescentParseException( Grammar.TERMINAL failure, List<Grammar.TERMINAL> remaining) {
        msg = "ParseException: on terminal " + failure + ", " + remaining + " remaining";
    }


    private static final long serialVersionUID = 753221L;

    private String msg;

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
