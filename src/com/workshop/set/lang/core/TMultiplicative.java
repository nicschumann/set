package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Collection;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TMultiplicative implements Pattern {
    public TMultiplicative( TScalar s, Pattern v ) {
        this.scalar = s;
        this.multiplicand = v;
    }

    public final TScalar scalar;
    public final Pattern multiplicand;

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
    public Term type( Context gamma ) throws TypecheckingException {
        try {
            TField f = ((TField)multiplicand.type( gamma ));
            return f;
        } catch ( ClassCastException _ ) {
            return null;
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
    public Pattern substitute( Term x, TNameGenerator.TName y ) {
        return new TMultiplicative( scalar, multiplicand.substitute(x,y) );
    }

    @Override
    public Set<HasValue> bind( Term value ) {
        return null;
    }

    @Override
    public Set<TNameGenerator.TName> names( ) {
        return null;
    }

    @Override
    public boolean binds( TNameGenerator.TName n ) {
        return multiplicand.binds( n );
    }
}
