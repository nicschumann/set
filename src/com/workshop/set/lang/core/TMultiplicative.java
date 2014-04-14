package com.workshop.set.lang.core;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.engines.Decide;
import com.workshop.set.lang.exceptions.EvaluationException;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;


import java.util.*;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TMultiplicative implements Pattern {
    public TMultiplicative( TScalar s, Term v ) {
        this.scalar = s;
        this.multiplicand = v;
    }

    public final TScalar scalar;
    public final Term multiplicand;

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence(this, (TMultiplicative) o, new HashSet<Symbol>(), new HashSet<Symbol>());
        } catch ( ClassCastException _ ) {
            return false;
        }
    }



    @Override
    public String toString() {
        return scalar.toString() + " * " + multiplicand.toString();
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
            throws ProofFailureException, TypecheckingException {
                Term t = gamma.proves( multiplicand );
                       gamma.compute( this, t );
                return gamma.extend(this, t);
    }

    @Override
    public Term reduce() throws EvaluationException {
        return new TAdditive( scalar, multiplicand.reduce() );
    }

    @Override
    public Pattern substitute( Term x, Symbol y ) {
        return new TAdditive( scalar, multiplicand.substitute( x,y ) );
    }

    @Override
    public boolean binds( Symbol n ) {
        try {
            return ((Pattern)multiplicand).binds( n );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Set<Symbol> free() {
        return multiplicand.free();
    }

    @Override
    public Set<HasValue> bind( Term value )
        throws PatternMatchException {
        return multiplicand.bind( value );
    }

    @Override
    public Set<Judgement<Term>> decompose( Environment<Term> gamma )
            throws TypecheckingException, ProofFailureException {
        try {
            return ((Pattern)multiplicand).decompose( gamma );
        } catch ( ClassCastException _ ) {
            return new HashSet<Judgement<Term>>();
        }

    }

    @Override
    public int hashCode() {

        int a   = multiplicand.hashCode();
        int b   = (int)scalar.index;

        return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31)));

    }

    @Override
    public Set<Symbol> names() {
        try {
            return ((Pattern)multiplicand).names();
        } catch ( ClassCastException _ ) {
            return new LinkedHashSet<Symbol>();
        }
    }

}
