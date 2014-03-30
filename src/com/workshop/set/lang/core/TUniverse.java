package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Value;
import com.workshop.set.interfaces.Term;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TUniverse implements Term {

    public TUniverse( long level ) { this.level = level; }
    public final long level;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TUniverse)o).level == this.level;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return  ( level == 0L ) ? "Univ" : "Univ{" + Long.toString( level ) + "}";
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
