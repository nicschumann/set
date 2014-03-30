package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TMultiplicative implements Pattern {
    public TMultiplicative( TScalar s, Pattern v ) {
        this.scalar = s;
        this.multiplicand = v;
    }

    public final TScalar scalar;
    public final Pattern multiplicand;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TMultiplicative)o).scalar.equals(this.scalar)
                && ((TMultiplicative)o).multiplicand.equals(this.multiplicand);
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return scalar.toString() + " * " + multiplicand.toString();
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
