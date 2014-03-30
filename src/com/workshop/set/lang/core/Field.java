package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Value;
import com.workshop.set.interfaces.Term;

/**
 * Created by nicschumann on 3/29/14.
 */
public class Field implements Term {

    public Field( long exponent ) { this.exponent = exponent; }
    public final long exponent;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((Field)o).exponent == this.exponent;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return ( exponent == 0L ) ? "F" : "F{"+ Long.toString( exponent ) + "}";
    }

    @Override
    public Term typesAs( Context gamma ) {
        return null;
    }

    @Override
    public Value evaluatesTo( Environment eta ) {
        return null;
    }
}
