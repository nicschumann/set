package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TJudgement implements Term {
    public TJudgement(Pattern T1, Pattern T2) {
        left = T1; right = T2;
    }

    public final Pattern left;
    public final Pattern right;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TJudgement)o).left.equals( left )
                && ((TJudgement)o).right.equals( right );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return left.toString() + " = " + right.toString();
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
