package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
            return ((TMultiplicative)o).scalar.equals(this.scalar)
                && ((TMultiplicative)o).multiplicand.equals(this.multiplicand);
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return scalar.toString() + " * " + multiplicand.toString();
    }

    @Override
    public Context type( Context gamma ) throws TypecheckingException {
            return multiplicand.type( gamma );
    }

    @Override
    public Term step( Environment eta ) {
        // TODO : define small step evaluation
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        // TODO : define big step evaluation
        return null;
    }

    @Override
    public Pattern substitute( Term x, TNameGenerator.TName y ) {
        return new TAdditive( scalar, multiplicand.substitute( x,y ) );
    }

    @Override
    public boolean binds( TNameGenerator.TName n ) {
        try {
            return ((Pattern)multiplicand).binds( n );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Set<HasValue> bind( Term value )
        throws PatternMatchException {
        return multiplicand.bind( value );
    }

    @Override
    public Set<Judgement> decompose( Context gamma )
            throws TypecheckingException {
        try {
            return ((Pattern)multiplicand).decompose( gamma );
        } catch ( ClassCastException _ ) {
            return new HashSet<Judgement>();
        }

    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TMultiplicative;
    }

    @Override
    public int hashCode() {

        int a   = multiplicand.hashCode();
        int b   = (int)scalar.index;

        return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32)));

    }

    @Override
    public Set<TNameGenerator.TName> names() {
        try {
            return ((Pattern)multiplicand).names();
        } catch ( ClassCastException _ ) {
            return new HashSet<TNameGenerator.TName>();
        }
    }

}
