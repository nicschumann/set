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
public class TField implements Term {

    @Override
    public boolean equals( Object o ) {
        return o instanceof TField;
    }

    @Override
    public String toString() {
        return "F";
    }

    @Override
    public Context type( Context gamma ) {
        return gamma.extend(new HasType( this, new TUniverse(0L) ) );
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
    public Term substitute( Term x, TNameGenerator.TName y ) {
        return this;
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TField;
    }

    @Override
    public int hashCode() {
        return 1;
    }

}
