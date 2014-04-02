package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
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
            return ((TCollection)o).contents.equals( contents )
                && ((TCollection)o).cardinality == cardinality;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        gamma.step();

        gamma = contents.type( gamma );

        gamma.unstep();

        return gamma.extend( this, gamma.proves( contents ) );
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
    public boolean kind( Term t ) {
        return t instanceof TCollection;
    }

    @Override
    public int hashCode() {

        int a   = contents.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 32))) + (cardinality ^ (cardinality >>> 32)));

    }

}
