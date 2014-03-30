package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;

import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TAbstraction implements Term {
    public TAbstraction(Pattern V, Term TY, Term T ) {
        binder = V;
        type = TY;
        body = T;
    }

    public final Pattern binder;
    public final Term type;
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
        return "lambda " + binder.toString() + " -> " + body.toString();
    }

    @Override
    public Term type( Context gamma )
        throws TypecheckingException {
        try {

            TUniverse U1            = (TUniverse)type.type( gamma );
            Term T2                 = body.type( gamma.extend( new HasType( binder, type ) ) );
            TUniverse U2            = (TUniverse)body.type( gamma.extend( new HasType( binder, type ) ) );

            if ( U1.equals( U2 ) ) {
                return new TAll( binder, type, T2 );
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
    public Term substitute( Term x, TNameGenerator.TName y ) {
        if ( !binder.binds( y ) ) {
            return new TAbstraction( binder, type, body.substitute(x,y) );
        } else {
            return this;
        }
    }

}
