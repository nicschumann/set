package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.judgements.HasType;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TField implements Term {

    @Override
    public boolean equals( Object o ) {
        try {
            TField _ = (TField)o;
            return true;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "F";
    }

    @Override
    public Term type( Context gamma ) {
        return new TUniverse( 0L );
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

}
