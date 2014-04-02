package com.workshop.set.lang.core;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
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
        for ( Term comp : entries ) { s.append( comp.toString() + " " ); }
        s.append( ")" );
        return s.toString();
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {

            boolean fold = true;
            Term t = (entries.get( 0 ).type( gamma )).proves( entries.get( 0 ) );

            for ( int i = 1; i < entries.size(); i++ ) {
                Term tprime = (entries.get( i ).type( gamma )).proves( entries.get( i ) );

                fold &= t.equals( tprime );
                if ( !fold ) throw new TypecheckingException( this, gamma );
                t = tprime;
            }

            return gamma.extend( this, new TExponential( t, entries.size() ) );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }

    }

    @Override
    public Pattern substitute( Term x, Symbol y ) {
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
    public boolean binds( Symbol n ) {
        boolean fold = false;
        for ( Term t : entries ) {
            try {
                fold |= ((Pattern)t).binds( n );
                if ( fold ) break;
            } catch ( ClassCastException _ ) {}
        }
        return fold;
    }

    public Set<Judgement<Term>> decompose( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {
            Set<Judgement<Term>> j = new HashSet<Judgement<Term>>();

            Term ty = gamma.proves( this ); // retrieve the type of this
            Integer arity = ((TExponential)ty).exp;
            Term base = ((TExponential)ty).base;

            for ( int i = 0; i < arity; i++ ) {
                try {
                    j.addAll(((Pattern) entries.get(i)).decompose( gamma.extend( entries.get(i), base )));
                } catch ( ClassCastException _ ) {}
             }

            return j;

        } catch ( ArrayIndexOutOfBoundsException _ ) {
            throw new TypecheckingException( this, gamma, "Decomposition Error" );
        }
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TVector;
    }

    @Override
    public int hashCode() {

        int a   = entries.hashCode();

        return 37 * (37 * (a ^ (a >>> 31))) + 1;

    }

    @Override
    public Set<TNameGenerator.TName> names() {
        Set<TNameGenerator.TName> n = new HashSet<TNameGenerator.TName>();
        for ( Term t : entries ) {
            try {
                n.addAll( ((Pattern)t).names() );
            } catch ( ClassCastException _ ) {}
        }
        return n;
    }
}
