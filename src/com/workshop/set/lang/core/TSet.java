package com.workshop.set.lang.core;

import java.util.Collection;
import java.util.ArrayList;

import com.workshop.set.interfaces.*;

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
