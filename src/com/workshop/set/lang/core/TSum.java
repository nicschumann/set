package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/31/14.
 */
public class TSum implements Term {
    public TSum( Pattern binder, Term type, Term body ) {
        this.binder = binder;
        this.type = type;
        this.body = body;
    }

    public final Pattern binder;
    public final Term type;
    public final Term body;

    @Override
    public String toString() {
        return "sum " + binder.toString() + " : " + type.toString() + " . " + body.toString();
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TSum)o).type.equals( type )
                && ((TSum)o).body.equals( body );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
            throws ProofFailureException, TypecheckingException {
        try {

            gamma.step();

            TUniverse U1 = (TUniverse)(type.type( gamma )).proves( type );
            TUniverse U2 = (TUniverse)(body.type( gamma.extend( binder, type ) )).proves( body );

            gamma.unstep();

            return gamma.extend( this, U1.max(U2) );

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        if ( !binder.binds( y ) ) {
            return new TSum( binder, type.substitute(x,y), body.substitute( x,y ) );
        } else return new TSum( binder, type.substitute(x,y), body );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TSum;
    }

    @Override
    public int hashCode() {

        int a   = binder.hashCode();
        int b   = type.hashCode();
        int c   = body.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32))) + (c ^ (c >>> 32)) + 3;

    }

}
