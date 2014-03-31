package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Term;
import com.workshop.set.interfaces.Value;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/30/14.
 */
public class TCollection implements Term {
    public TCollection( Term t, int l ) {
        contents = t;
        cardinality = l;
    }

    private final Term contents;
    private final int cardinality;

    @Override
    public String toString() {
        return contents.toString();
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TCollection)o).contents.equals( contents )
                && ((TCollection)o).cardinality == cardinality;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Term type( Context gamma )
        throws TypecheckingException {
        return contents.type( gamma );
    }

    @Override
    public Term step( Environment eta ) {
        // TODO : define small step evaluation
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        // TODO : define big step evaluation
        return null;
    }

    @Override
    public Term substitute( Term x, TNameGenerator.TName y ) {
        return contents.substitute( x,y );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

}
