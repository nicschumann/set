package com.workshop.set.lang.core;

import java.util.Set;
import java.util.Vector;

import com.workshop.set.interfaces.*;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TVector implements Pattern {
    public TVector( Vector<TScalar> entries ) {
        this.entries = entries;
    }

    private final Vector<TScalar> entries;

    public Vector<TScalar> components() {
        return new Vector<TScalar>( entries );
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TVector)o).entries.equals( entries );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("( ");
        for ( TScalar scalar : entries ) { s.append( scalar.toString() ); }
        s.append( ")" );
        return s.toString();
    }

    @Override
    public Term type( Context gamma ) {
        return new TExponential( new TField(), entries.size() );
    }

    @Override
    public Term step( Environment eta ) {
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        return null;
    }

    @Override
    public Set<Judgement> bind( Term value ) {
        return null;
    }
}
