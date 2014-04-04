package com.workshop.set.lang.core;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;
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
            TAll tar = (TAll)o;
            return  ((TAll)o).type.equals(type)
                    && ((TAll)o).body.equals( body );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        if ( Sets.intersection(binder.free(),body.free()).isEmpty() ) {
            return "(" + type.toString() + " -> " + body.toString() + ")";
        } else return "all " + binder.toString() + " : " + type.toString() + " . " + body.toString();
    }

    @Override
    /**
     * A forall takes
     *
     *
     *               t -[e,x:T]-> t'
     * --------------------------------------- ( TAll )
     *   all x : T . t -[e]-> all x : T . t'
     *
     *
     * @param gamma, the environment to evaluate this term in
     * @return
     */
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {

            gamma.step();

            TUniverse U1 = (TUniverse)(type.type( gamma )).proves( type );

            gamma.extend( binder, type );

            Set<Judgement<Term>> bound          = binder.decompose( gamma );

            TUniverse U2 = (TUniverse)(body.type( gamma.extend( bound ) ) ).proves( type );

            gamma.unstep();

            TUniverse max = U1.max(U2);

                   gamma.compute( this, max );
            return gamma.extend( this, max );

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    @Override
    public Set<Symbol> free() {
        Set<Symbol> bound = binder.free();
        Set<Symbol> ty = type.free();
        Set<Symbol> bd = body.free();
        return Sets.difference( Sets.union( ty, bd ), bound );
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        if ( !binder.binds( y ) ) {
            return new TAll( binder, type.substitute(x,y), body.substitute( x,y ) );
        } else return new TAll( binder, type.substitute(x,y), body );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {

        int a   = binder.hashCode();
        int b   = type.hashCode();
        int c   = body.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31))) + (c ^ (c >>> 31)) + 2;

    }
}
