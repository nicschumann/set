package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Term;
import com.workshop.set.interfaces.Value;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;
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

    public final Term contents;
    private final int cardinality;

    @Override
    public String toString() {
        return "Set["+contents.toString()+"]";
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
    public Context type( Context gamma )
        throws TypecheckingException {
        Context gamma1 = contents.type( gamma );
        return gamma1.extend( new HasType( this, gamma1.proves( contents ) ) );
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

    @Override
    public boolean kind( Term t ) {
        return t instanceof TCollection;
    }

    @Override
    public int hashCode() {

        int a   = contents.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 32))) + (cardinality ^ (cardinality >>> 32)));

    }

}
