package com.workshop.set.lang.core;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.core.TNameGenerator.TName;

import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.*;

/**
 * Implements a Scalar value of some Field indexed by the Real Numbers
 *
 * Created by nicschumann on 3/29/14.
 */
public class TScalar implements Pattern {
    public TScalar( double index ) {
        this.index = index;
    }

    public final double index;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TScalar)o).index == this.index;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return Double.toString( index );
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException {
               TField f = new TField();

               gamma.compute( this, f );
        return gamma.extend( this, f );
    }

    @Override
    public Term reduce() {
        return this;
    }

    @Override
    public Pattern substitute( Term x, Symbol y ) { return this; }

    @Override
    public Set<Symbol> free() {
        return new HashSet<Symbol>();
    }

    @Override
    public Set<HasValue> bind( Term t ) {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean binds( Symbol n ) {
        return false;
    }

    @Override
    public Set<Judgement<Term>> decompose( Environment<Term> gamma ) {
        return new HashSet<Judgement<Term>>();
    }

    @Override
    public int hashCode() {

        int a   = (int)index;


        return 37 * (37 * (a ^ (a >>> 31)));

    }

    @Override
    public Set<Symbol> names() {
        return new LinkedHashSet<Symbol>();
    }

}
