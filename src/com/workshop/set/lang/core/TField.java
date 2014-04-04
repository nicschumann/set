package com.workshop.set.lang.core;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;

import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TField implements Term {

    @Override
    public boolean equals( Object o ) {
        return o instanceof TField;
    }

    @Override
    public String toString() {
        return "F";
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException {
         TUniverse ty = new TUniverse(0L);
         return gamma.extend( gamma.compute( this, ty ), ty );

    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return this;
    }

    @Override
    public Set<Symbol> free() {
        return new HashSet<Symbol>();
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {
        return 1;
    }

}
