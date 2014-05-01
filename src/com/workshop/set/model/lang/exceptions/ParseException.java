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
    public ParseException( String msg, Collection<TERMINAL> parsed, Collection<TERMINAL> remaining ) {
        this.msg = "ParseError:" + System.lineSeparator();
        this.msg += msg + System.lineSeparator();
        this.msg += "parser position: " + parsed + " $ " + remaining + System.lineSeparator();
    }

    private static final long serialVersionUID = 4566322L;

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
