package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

import java.util.Objects;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TAbstraction implements Term {
    public TAbstraction(Pattern V, Term T ) {
        binder = V;
        body = T;
    }

    public final Pattern binder;
    public final Term body;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TAbstraction)o).binder.equals( binder )
                && ((TAbstraction)o).body.equals( body );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "lambda " + binder.toString() + " . " + body.toString();
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
