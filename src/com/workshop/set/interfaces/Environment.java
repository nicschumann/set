package com.workshop.set.interfaces;

import com.workshop.set.lang.exceptions.ProofFailureException;

import java.util.Set;

/**
 * An environment maintains a mapping of variable names to values,
 * A mapping of values to to scalar coordinates, and a
 */
public interface Environment<T> extends Context<T> {

    public Context<T> typing();
    public Context<T> evaluation();

    public Term compute( Term t, Term TY ) throws ProofFailureException;
    public Term value() throws ProofFailureException;

    /* Override the Context's methods to return the more specified type */
    public Environment<T> extend( T x, T T );
    public Environment<T> extend( Set<Judgement<T>> es );
    public Environment<T> step();
    public Environment<T> unstep();

}
