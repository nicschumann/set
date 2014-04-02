package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
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
            return ((TJudgement)o).left.equals( left )
                && ((TJudgement)o).right.equals( right );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {
            gamma.step();

            Term T1 = (left.type( gamma )).proves( left );
            Term T2 = (right.type( gamma )).proves( right );


            if ( T1.equals( T2 ) ) {
                gamma.unstep();
                return gamma.extend( this, T1 );
            } throw new TypecheckingException( this, gamma );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return new TJudgement( left.substitute(x,y), right.substitute(x,y) );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TJudgement;
    }

    @Override
    public int hashCode() {

        int a   = left.hashCode();
        int b   = right.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31)));

    }
}
