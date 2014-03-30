package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Term;
import com.workshop.set.interfaces.Value;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TApplication implements Term {
    public TApplication( Term T1, Term T2 ) {
        implication = T1;
        argument = T2;
    }

    public final Term implication;
    public final Term argument;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TApplication)o).implication.equals( implication )
                && ((TApplication)o).argument.equals( argument );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return implication.toString() + " " + argument.toString();
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
