package com.workshop.set.model.interfaces;

import java.util.Set;

import com.workshop.set.model.lang.exceptions.ProofFailureException;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Context<T> {

    public boolean contains( T t );
    /**
     * This methods extends this context with the supplied judgement.
     *
     * @param x the term to accept into the Context,
     * @param T the type to annotate x with.
     * @return the new, extended context.
     */
    public Context<T> extend( T x, T T );
    public Context<T> extend( Set<Judgement<T>> es );

    /**
     * given a term a, this context returns the type that it ascribes to a,
     * or null, if that term is not among this context's judgements.
     * @param a a term to retrieve the type of.
     * @return a's type, or null
     */
    public T proves( T a ) throws ProofFailureException;
    public T provesAt( int i, T a ) throws ProofFailureException;

    /**
     * Pick a fresh name that is unique in this context
     * @param s a string suggestion to help in reading the symbol.
     * @return a new symbol that is unique with respect to this context, based on s
     */
    public Symbol freshname( String s );
    public boolean wellformed();

    /**
     * This method tells the current derivation to take a step,
     * effectively incrementing it's depth in the derivation tree.
     *
     * @return the new, stepped context.
     */
    public Context<T> step();
    public Context<T> unstep();
}
