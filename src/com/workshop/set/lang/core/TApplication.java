package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

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
    public Term type( Context gamma )
        throws TypecheckingException {
        try {
            TAll All = (TAll)implication.type( gamma );
            Term T1 = argument.type( gamma );

            if ( All.type.equals( T1 ) ) {
                Term T = All.body;
                for (HasValue j : All.binder.bind( argument ) ) {
                    T = T.substitute( j.environment(), j.inhabitant() );
                }
                return T;
            } else throw new TypecheckingException( this, gamma );

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
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
    public Term substitute( Term x, TNameGenerator.TName y) {
        return new TApplication( implication.substitute(x,y), argument.substitute(x,y) );
    }
}
