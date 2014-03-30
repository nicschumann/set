package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

/**
 *
 *
 *      s + V                   ( V-Op1 )
 *
 *
 */
public class TAdditive implements Pattern {
    public TAdditive( TScalar s, Pattern v ) {
        this.scalar = s;
        this.addand = v;
    }

    public final TScalar scalar;
    public final Pattern addand;


    @Override
    public boolean equals( Object o ) {
        try {
            return ((TAdditive)o).scalar.equals(this.scalar)
                    && ((TAdditive)o).addand.equals(this.addand);
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return scalar.toString() + " + " + addand.toString();
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
