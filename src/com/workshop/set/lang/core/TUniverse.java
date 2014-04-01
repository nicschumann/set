package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

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

    public TUniverse max( TUniverse n ) {
        return ( level > n.level ) ? this : n;
    }

    @Override
    /**
     * All well-formed contexts prove that Univ{n} : Univ{n+1}
     */
    public Context type( Context gamma ) {
        return gamma.extend( new HasType( this, new TUniverse( level + 1L ) ) );
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
    public Term substitute( Term x, TNameGenerator.TName y ) {
        return this;
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TUniverse;
    }

    @Override
    public int hashCode() {

        int a   = (int)level;

        return 37 * (37 * (a ^ (a >>> 32)));

    }
}
