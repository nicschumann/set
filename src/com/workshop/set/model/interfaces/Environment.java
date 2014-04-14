package com.workshop.set.model.interfaces;

import com.workshop.set.model.lang.exceptions.ProofFailureException;

import java.util.Set;

/**
 * An environment maintains a mapping of variable names to values,
 * A mapping of values to to scalar coordinates, and a
 */
public interface Environment<T> extends Context<T> {

    public Context<T> typing();
    public Context<T> evaluation();

    public T compute( T t, T TY ) throws ProofFailureException;
    public T value() throws ProofFailureException;

    /* Override the Context's methods to return the more specified type */
    public Environment<T> extend( T x, T T );
    public Environment<T> extend( Set<Judgement<T>> es );
    public Environment<T> step();
    public Environment<T> unstep();

}
