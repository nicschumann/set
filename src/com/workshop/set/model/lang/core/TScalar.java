package com.workshop.set.model.lang.core;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Judgement;
import com.workshop.set.model.interfaces.Pattern;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.judgements.HasValue;

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
