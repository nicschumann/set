package com.workshop.set.lang.core;

import com.google.common.collect.Sets;
import com.workshop.set.interfaces.*;
import com.workshop.set.lang.engines.Decide;
import com.workshop.set.lang.exceptions.EvaluationException;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/31/14.
 */
public class TSum implements Term,Value {
    public TSum( Pattern binder, Term type, Term body ) {
        this.binder = binder;
        this.type = type;
        this.body = body;
    }

    public Pattern binder;
    public Term type;
    public Term body;

    @Override
    public String toString() {
        if ( Sets.intersection(binder.free(),body.free()).isEmpty() ) {
            return "(" + type.toString() + " \u2A2F " + body.toString() + ")";
        } else return "\u03A3 " + binder.toString() + " : " + type.toString() + " . " + body.toString();
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence(this, (TSum) o, new HashSet<Symbol>(), new HashSet<Symbol>());
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

            gamma.extend( binder, type );

            Set<Judgement<Term>> bound          = binder.decompose( gamma );

            TUniverse U2 = (TUniverse)(body.type( gamma.extend( bound ) )).proves( body );

            gamma.unstep();

            TUniverse max = U1.max( U2 );

                   gamma.compute( this, max );
            return gamma.extend( this, max );

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma, "!!!" );
        }
    }

    @Override
    public Term reduce() throws EvaluationException {
        return new TAll( binder,type,body.reduce() );
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
    public Set<Symbol> free() {
        Set<Symbol> bound = binder.free();
        Set<Symbol> ty = type.free();
        Set<Symbol> bd = body.free();
        return Sets.difference( Sets.union( ty, bd ), bound );
    }

    @Override
    public int hashCode() {

        int a   = binder.hashCode();
        int b   = type.hashCode();
        int c   = body.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31))) + (c ^ (c >>> 31)) + 3;

    }

}
