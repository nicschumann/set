package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents an Exponential Type :
 *
 *    G |- a : T            n in N
 * -------------------------------------------
 *              a ^ n : Univ0
 *
 */
public class TExponential implements Term {
    public TExponential( Term base, Integer exponent ) {
        this.base   = base;
        this.exp    = exponent;
    }

    public final Term base;
    public final Integer exp; // change to scalar?

    @Override
    public String toString() {
        return base.toString() + "^" + exp.toString();
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TExponential)o).base.equals( base )
                && ((TExponential)o).exp.equals( exp );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {

        gamma = base.type( gamma );

        return gamma.extend( this, gamma.proves( base ) );
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return new TExponential( base.substitute(x,y), exp );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TExponential;
    }

    @Override
    public int hashCode() {

        int a   = base.hashCode();
        int b   = exp;

        return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32)));

    }

}
