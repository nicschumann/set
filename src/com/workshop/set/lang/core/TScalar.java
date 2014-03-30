package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

/**
 * Implements a Scalar value of some Field indexed by the Real Numbers
 *
 * Created by nicschumann on 3/29/14.
 */
public class TScalar implements Pattern {
    public TScalar( double index ) {
        this.index = index;
    }

    public final double index;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TScalar)o).index == this.index;
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return Double.toString( index );
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
