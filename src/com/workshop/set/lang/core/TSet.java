package com.workshop.set.lang.core;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TSet<V extends Pattern> implements Pattern {
    public TSet( Collection<V> elements ) {
        this.elements = new ArrayList<V>( elements );
    }

    private ArrayList<V> elements;

    private ArrayList<V> elements() {
        return new ArrayList<V>( elements );
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TSet<V>)o).elements().equals( elements );
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
    public Term type( Context gamma )
        throws TypecheckingException {

        ArrayList<Term> types = new ArrayList<Term>();
        boolean fold = true;
        Term t = null;

        for ( V e : elements ) { types.add( e.type( gamma ) ); }
        for ( int i = 0; i < types.size() - 1; i++ ) {
            t = types.get( i );
            fold &= types.get( i ).equals( types.get( i+1 ) );
        }

        if ( fold ) {
            return (t == null) ? types.get( 0 ) : t;
        } throw new TypecheckingException( this, gamma );

    }

    @Override
    public Term step( Environment eta ) {
        // TODO : define small step evaluation
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        return null;
    }

    @Override
    public Pattern substitute( Term x, TNameGenerator.TName y ) {
        Collection<Pattern> es = new ArrayList<Pattern>();
        for ( V e : elements ) {
            es.add( e.substitute( x,y ) );
        }
        return new TSet<Pattern>( es );
    }

    @Override
    public Set<HasValue> bind( Term value ) {
        return null;
    }

    @Override
    public boolean binds( TNameGenerator.TName name ) {
        boolean fold = false;
        for ( V e : elements ) {
            fold |= e.binds( name );
            if ( fold ) break;
        }
        return fold;
    }

    @Override
    public Set<TNameGenerator.TName> names( ) {
        HashSet<TNameGenerator.TName> union = new HashSet<TNameGenerator.TName>();
        for ( V e : elements ) {
            union.addAll( e.names() );
        }
        return union;
    }
}
