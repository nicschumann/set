package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Value;
import com.workshop.set.interfaces.Term;

/**
 * Created by nicschumann on 3/29/14.
 */
public class Universe implements Term {

    public Universe( long level ) { this.level = level; }
    public final long level;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((Universe)o).level == this.level;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return  ( level == 0L ) ? "Univ" : "Univ{" + Long.toString( level ) + "}";
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
