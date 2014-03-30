package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TTuple implements Pattern {
    public TTuple( Pattern domain, Pattern range ) {
        this.domain = domain;
        this.range = range;
    }

    public final Pattern domain;
    public final Pattern range;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TTuple)o).domain.equals( domain )
                && ((TTuple)o).range.equals( range );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "( " + domain.toString() + ", " + range.toString() + " )";
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
