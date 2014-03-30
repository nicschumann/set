package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

import java.util.ArrayList;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TVector implements Pattern {
    public TVector( java.util.Vector<TScalar> entries ) {
        this.entries = entries;
    }

    private final java.util.Vector<TScalar> entries;

    public java.util.Vector<TScalar> components() {
        return new java.util.Vector<TScalar>( entries );
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
        return null;
    }

    @Override
    public Term step( Environment eta ) {
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        return null;
    }
}
