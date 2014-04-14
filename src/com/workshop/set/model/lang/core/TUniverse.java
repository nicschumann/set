package com.workshop.set.model.lang.core;

import com.workshop.set.model.interfaces.*;
import com.workshop.set.model.lang.exceptions.PatternMatchException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.judgements.HasType;
import com.workshop.set.model.lang.judgements.HasValue;

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
        return  ( level == 0L ) ? "*" : "*[" + Long.toString( level ) + "]";
    }

    public TUniverse max( TUniverse n ) {
        return ( level > n.level ) ? this : n;
    }

    @Override
    /**
     * All well-formed contexts prove that Univ{n} : Univ{n+1}
     */
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException {
        TUniverse t = new TUniverse( level + 1L );
        return gamma.extend( gamma.compute( this, t ), t );
    }

    @Override
    public Term reduce() {
        return new TUniverse( level );
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return this;
    }

    @Override
    public Set<Symbol> free() {
        return new HashSet<Symbol>();
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {

        int a   = (int)level;

        return 37 * (37 * (a ^ (a >>> 32)));

    }
}
