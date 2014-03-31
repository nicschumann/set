package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.core.TNameGenerator.TName;
import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    public Term type( Context gamma ) {
        return new TField( );
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
    public Pattern substitute( Term x, TName y ) { return this; }

    @Override
    public Set<HasValue> bind( Term t ) {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean binds( TName n ) {
        return false;
    }

    @Override
    public Set<Judgement> decompose( Context gamma ) {
        return new HashSet<Judgement>();
    }

}
