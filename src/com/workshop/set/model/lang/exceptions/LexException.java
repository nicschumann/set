package com.workshop.set.model.lang.exceptions;

/**
 * Created by nicschumann on 4/21/14.
 */
public class LexException extends Exception {
    public LexException( int brackets, int parens, int braces ) {
        this.brackets = brackets;
        this.parens = parens;
        this.braces = braces;
    }

    private final int brackets;
    private final int parens;
    private final int braces;

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public String getMessage() {
        return "LexException: "+parens+" Mismatched Parentheses; "+brackets+" Mismatched Brackets; "+braces+" Mismatched Braces.";
    }
}
