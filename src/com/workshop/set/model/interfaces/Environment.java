package com.workshop.set.model.interfaces;

import java.util.Set;

import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;

/**
 * An environment maintains a mapping of variable names to values,
 * A mapping of values to to scalar coordinates, and a
 */
public interface Environment<T> extends Context<T> {
    // Lang External Methods

    public T getValue( Symbol s ) throws ProofFailureException;
    public T getType( Symbol s ) throws ProofFailureException;
    public Environment<T> name( Symbol s, T t ) throws ProofFailureException, TypecheckingException;

    // Lang-Internal Methods
    public Context<T> typing();
    public Context<T> evaluation();

    public T compute( T t, T TY ) throws ProofFailureException;
    public T value() throws ProofFailureException;

    /* Override the Context's methods to return the more specified type */
    public Environment<T> extend( T x, T T );
    public Environment<T> extend( Set<Judgement<T>> es );
    public Environment<T> step();
    public Environment<T> unstep();

    public Symbol basename( String h );

}
