package com.workshop.set.lang.core;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Term;
import com.workshop.set.interfaces.Value;
import com.workshop.set.lang.exceptions.TypecheckingException;

/**
 * This class represents an Exponential Type :
 *
 *    G |- a : T            n in N
 * -------------------------------------------
 *              a ^ n : Univ0
 *
 */
public class TExponential implements Term {
    public TExponential( Term base, Integer exponent ) {
        this.base   = base;
        this.exp    = exponent;
    }

    private final Term base;
    private final Integer exp; // change to scalar?

    @Override
    public String toString() {
        return base.toString() + "^" + exp.toString();
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TExponential)o).base.equals( base )
                && ((TExponential)o).exp.equals( exp );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Term type( Context gamma )
        throws TypecheckingException {
        try {
            TUniverse t = (TUniverse)gamma.proves( base );
            return t;
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
        return new TExponential( base.substitute(x,y), exp );
    }

}
