package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Set;

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
    public Term type( Context gamma )
        throws TypecheckingException {
        try {
            TField f = ((TField)addand.type( gamma ));
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
        return new TAdditive( scalar, addand.substitute( x,y ) );
    }

    @Override
    public boolean binds( TNameGenerator.TName n ) {
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
}
