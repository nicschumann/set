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
 * Created by nicschumann on 3/29/14.
 */
public class TJudgement implements Term {
    public TJudgement(Term T1, Term T2) {
        left = T1; right = T2;
    }

    public final Term left;
    public final Term right;

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence(this, (TJudgement) o, new HashSet<Symbol>(), new HashSet<Symbol>());
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return left.toString() + " \u003D " + right.toString();
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {

            Term T1 = (left.type( gamma )).proves( left );
            Term T2 = (right.type( gamma )).proves( right );

            if ( T1.equals( T2 ) ) {
                       gamma.compute( this, T1 );
                return gamma.extend( this,  T1 );
            } throw new TypecheckingException( this, gamma );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    public Term reduce() throws EvaluationException {
        return new TJudgement( left.reduce(), right.reduce() );
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return new TJudgement( left.substitute(x,y), right.substitute(x,y) );
    }


    @Override
    public Set<Symbol> free() {
        return Sets.union(left.free(), right.free());
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {

        int a   = left.hashCode();
        int b   = right.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31)));

    }
}
