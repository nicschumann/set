package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.core.TNameGenerator.TName;
import com.workshop.set.lang.exceptions.EvaluationException;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

import java.lang.reflect.Type;
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
        return "all " + binder.toString() + " : " + type.toString() + " . " + body.toString();
    }

    @Override
    public Context type( Context gamma )
        throws TypecheckingException {
        try {

            TUniverse U1 = (TUniverse)(type.type( gamma )).proves( type );
            TUniverse U2 = (TUniverse)(body.type( gamma.extend( new HasType( binder, type )) )).proves( type );

            return gamma.extend(new HasType( this, U1.max(U2) ));

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    /**
     * A forall takes
     *
     *
     *               t -[e]-> t'
     * --------------------------------------- ( TAll )
     *   all x : T . t -[e]-> all x : T . t'
     *
     *
     * @param eta, the environment to evaluate this term in
     * @return
     */
    @Override
    public Term step( Environment eta )
        throws TypecheckingException, EvaluationException {
        // extend the environment with the type of this term.
        //Environment etaPrime = eta.extend( new HasType( this, this.type( eta.typing() ) ) );
        // return this abstraction after the body has taken one step.
        //return new TAll( binder, type, body.step( etaPrime ) );
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
        } else return new TAll( binder, type.substitute(x,y), body );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TAll;
    }

    @Override
    public int hashCode() {

        int a   = binder.hashCode();
        int b   = type.hashCode();
        int c   = body.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32))) + (c ^ (c >>> 32)) + 2;

    }
}
