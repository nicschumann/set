package com.workshop.set.lang.core;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.engines.Decide;
import com.workshop.set.lang.exceptions.EvaluationException;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/30/14.
 */
public class TCollection implements Term {
    public TCollection( Term t, int l ) {
        contents = t;
        cardinality = l;
    }

    public final Term contents;
    private final int cardinality;

    @Override
    public String toString() {
        return "Set["+contents.toString()+"]";
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence(this, (TCollection) o, new HashSet<Symbol>(), new HashSet<Symbol>());
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {

        gamma = contents.type( gamma );
        Term t = gamma.proves(contents);

               gamma.compute(this, t);
        return gamma.extend( this, t );
    }

    @Override
    public Term reduce() throws EvaluationException {
        return new TCollection( contents.reduce(), cardinality );
    }

    @Override
    public Set<Symbol> free() {
        return contents.free();
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return contents.substitute( x,y );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {

        int a   = contents.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 32))) + (cardinality ^ (cardinality >>> 32)));

    }

}
