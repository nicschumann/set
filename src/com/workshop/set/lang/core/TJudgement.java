package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
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
    public Term type( Context gamma )
        throws TypecheckingException {
        try {
            Term T1 = left.type( gamma );
            Term T2 = right.type( gamma );
            if ( T1.equals( T2 ) ) {
                return T1;
            } throw new TypecheckingException( this, gamma );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    @Override
    public Term step( Environment eta ) {
        // TODO : define small step evaluation
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        return null;
    }

    @Override
    public Term substitute( Term x, TNameGenerator.TName y ) {
        return new TJudgement( left.substitute(x,y), right.substitute(x,y) );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

}
