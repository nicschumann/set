package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator;

import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Context {

    public boolean contains( Term t );
    /**
     * This methods extends this context with the supplied judgement.
     *
     * @param e the judgement to extend this context with.
     * @return the new, extended context
     */
    public Context extend( Judgement e );
    public Context extend( Set<Judgement> es );

    /**
     * given a term a, this context returns the type that it ascribes to a,
     * or null, if that term is not among this context's judgements.
     * @param a a term to retrieve the type of.
     * @return a's type, or null
     */
    public Term proves( Term a );

    public TNameGenerator.TName freshname( String s );

    public boolean wellformed();
}
