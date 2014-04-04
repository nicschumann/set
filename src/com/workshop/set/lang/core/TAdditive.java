package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;

import java.util.*;

/**
 *
 *
 *      s + V                   ( V-Op1 )
 *
 *
 */
public class TAdditive implements Pattern {
    public TAdditive( TScalar s, Term v ) {
        this.scalar = s;
        this.addand = v;
    }

    public final TScalar scalar;
    public final Term addand;


    @Override
    public boolean equals( Object o ) {
        try {
            return ((TAdditive)o).scalar.equals(this.scalar)
                && ((TAdditive)o).addand.equals(this.addand);
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return scalar.toString() + " + " + addand.toString();
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {

            Term t = gamma.proves( addand );
                   gamma.compute( this, t );
            return gamma.extend( this, t );

    }

    @Override
    public Set<Symbol> free() {
        return addand.free();
    }

    @Override
    public Pattern substitute( Term x, Symbol y ) {
        return new TAdditive( scalar, addand.substitute( x,y ) );
    }

    @Override
    public boolean binds( Symbol n ) {
        try {
            return ((Pattern)addand).binds( n );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Set<HasValue> bind( Term value )
        throws PatternMatchException {
        return addand.bind( value );
    }

    @Override
    public Set<Judgement<Term>> decompose( Environment<Term> gamma )
            throws TypecheckingException, ProofFailureException {
        try {
            return ((Pattern)addand).decompose( gamma );
        } catch ( ClassCastException _ ) {
            return new HashSet<Judgement<Term>>();
        }

    }


    @Override
    public int hashCode() {

        int a   = addand.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 31))));

    }

    @Override
    public Set<Symbol> names() {
        try {
            return ((Pattern)addand).names();
        } catch ( ClassCastException _ ) {
            return new LinkedHashSet<Symbol>();
        }
    }
}
