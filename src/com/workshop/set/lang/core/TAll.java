package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.core.TNameGenerator.TName;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TAll implements Term {
    public TAll(Pattern N, Term T1, Term T2 ) {
        binder = N;
        type = T1;
        body = T2;
    }

    public final Pattern binder;
    public final Term type;
    public final Term body;

    @Override
    public boolean equals( Object o ) {
        try {
            return  ((TAll)o).type.equals(type)
                    && ((TAll)o).body.equals( body );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "forall " + binder.toString() + " : " + type.toString() + " . " + body.toString();
    }

    @Override
    public Term type( Context gamma )
        throws TypecheckingException {
        try {

            TUniverse U1 = (TUniverse)type.type( gamma );
            TUniverse U2 = (TUniverse)body.type( gamma.extend( new HasType( binder, type )) );

            return U1.max( U2 );

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
    public Term substitute( Term x, TName y ) {
        if ( !binder.binds( y ) ) {
            return new TAll( binder, type.substitute(x,y), body.substitute( x,y ) );
        } else return this;
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }
}
