package com.workshop.set.lang.core;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TVector implements Pattern {
    public TVector( Vector<Term> entries ) {
        this.entries = entries;
    }

    private final Vector<Term> entries;

    public Vector<Term> components() {
        return new Vector<Term>( entries );
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
        for ( Term comp : entries ) { s.append( comp.toString() ); }
        s.append( ")" );
        return s.toString();
    }

    @Override
    public Term type( Context gamma )
        throws TypecheckingException {
        try {
            boolean fold = true;
            Term t = entries.get( 0 ).type( gamma );

            for ( int i = 1; i < entries.size(); i++ ) {
                Term tprime = entries.get( i ).type( gamma );

                fold &= t.equals( tprime );
                if ( !fold ) throw new TypecheckingException( this, gamma );
                t = tprime;
            }

            return new TExponential( t, entries.size() );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }

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
    public Pattern substitute( Term x, TNameGenerator.TName y ) {
        Vector<Term> t = new Vector<Term>();
        for (Term e : entries ) {
            t.add( e.substitute( x,y ) );
        }
        return new TVector( t );
    }

    @Override
    public Set<HasValue> bind( Term value )
        throws PatternMatchException {
        try {
            Vector<Term> v = ((TVector)value).components();
            Set<HasValue> s = new HashSet<HasValue>();

            for ( Term entry : entries ) { // duplicates?
                s.addAll(entry.bind(v.remove(0)));
            }

            return s;

        } catch ( ClassCastException _ ) {
            throw new PatternMatchException( this, value );
        }
    }

    @Override
    public boolean binds( TNameGenerator.TName n ) {
        boolean fold = false;
        for ( Term t : entries ) {
            try {
                fold |= ((Pattern)t).binds( n );
                if ( fold ) break;
            } catch ( ClassCastException _ ) {}
        }
        return fold;
    }
}
