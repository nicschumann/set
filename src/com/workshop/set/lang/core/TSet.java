package com.workshop.set.lang.core;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;


public class TSet implements Pattern {
    public TSet( Collection<Term> elements ) {
        this.elements = new ArrayList<Term>( elements );
    }

    private ArrayList<Term> elements;

    public ArrayList<Term> elements() {
        return new ArrayList<Term>( elements );
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TSet)o).elements().equals( elements );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{ ");
        for ( int i = 0; i < elements.size(); i++ ) {
            if ( i == elements.size() - 1 ) {
                s.append( elements.get( i ) + " }" );
            } else {
                s.append( elements.get( i ) + ", " );
            }
        }
        return s.toString();
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {

        ArrayList<Term> types = new ArrayList<Term>();
        boolean fold = true;
        Term t = null;

        for ( Term e : elements ) { types.add( (e.type( gamma )).proves( e ) ); }

        for ( int i = 0; i < types.size() - 1; i++ ) {
            t = types.get( i );
            fold &= types.get( i ).equals( types.get( i+1 ) );
        }

        if ( fold ) {

            TCollection ty = (t == null) ? new TCollection( types.get( 0 ), elements.size() )
                                         : new TCollection( t, elements.size() );

            return gamma.extend(  this, ty );

        } throw new TypecheckingException( this, gamma, "Heterogeneous Set" );

    }

    @Override
    public Pattern substitute( Term x, Symbol y ) {
        Collection<Term> es = new ArrayList<Term>();
        for ( Term e : elements ) {
            es.add( e.substitute( x,y ) );
        }
        return new TSet( es );
    }

    @Override
    public Set<HasValue> bind( Term value )
        throws PatternMatchException {
        try {
            Set<HasValue> s = new HashSet<HasValue>();
            TSet v = (TSet)value;

            for ( int i = 0; i < elements.size(); i++ ) {
                s.addAll( elements.get( i ).bind( v.elements().get( i ) ) );
            }

            return s;
        } catch ( ClassCastException _ ) {
            throw new PatternMatchException(this, value);
        }
    }

    @Override
    public boolean binds( Symbol name ) {
        boolean fold = false;
        for ( Term e : elements ) {
            try {
                fold |= ((Pattern)e).binds( name );
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
            Term base = ((TCollection)ty).contents;

            for ( int i = 0; i < elements.size(); i++ ) {
                try {
                    j.addAll(((Pattern) elements.get(i)).decompose( gamma.extend( elements.get(i), base )));
                } catch ( ClassCastException _ ) {}
            }

            return j;

        } catch ( ArrayIndexOutOfBoundsException _ ) {
            throw new TypecheckingException( this, gamma, "Decomposition Error" );
        }
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TSet;
    }

    @Override
    public int hashCode() {

        int a   = elements.hashCode();
        return 37 * (37 * (a ^ (a >>> 31)));

    }

    @Override
    public Set<TNameGenerator.TName> names() {
        Set<TNameGenerator.TName> n = new HashSet<TNameGenerator.TName>();
        for ( Term t : elements ) {
            try {
                n.addAll( ((Pattern)t).names() );
            } catch ( ClassCastException _ ) {}
        }
        return n;
    }

}
