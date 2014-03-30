package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.TNameGenerator.TName;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TAll implements Term {
    public TAll(TName N, Term T1, Term T2 ) {
        binder = N;
        type = T1;
        body = T2;
    }

    public final TName binder;
    public final Term type;
    public final Term body;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TAll)o).binder.equals( binder )
                    && ((TAll)o).type.equals( type )
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
