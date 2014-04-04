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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TAbstraction implements Term,Value {
    public TAbstraction(Pattern V, Term TY, Term T ) {
        binder = V;
        type = TY;
        body = T;
    }

    public Pattern binder;
    public Term type;
    public Term body;

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence( this, (TAbstraction)o, new HashSet<Symbol>(), new HashSet<Symbol>() );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "\u03BB " + binder.toString() + " : (" + type.toString() +") \u21D2 [" + body.toString() + "]";
    }

    /**
     * A TAbstraction takes a step of evaluation by evaluating the terms inside its body,
     * with the assumption of its parameter have a certain type, while maintaining the structure
     * of its abstraction:
     *
     *                     t -[e,x:T]-> t'
     * -------------------------------------------------------        (TAbs)
     *    lambda x : T . t   -[e]->   lambda x : T . t'
     *
     *
     * @param gamma, the environment to evaluate this term in
     * @return the term t' that this term t steps to under reduction.
     */
    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {

            try {
                    //System.out.println( "Typing an Abstraction" );

                                                    gamma.step();                           // step the current environment


                gamma                               = type.type( gamma );                   // type the type of the formal parameter in the current environment -
                                                                                            // at this point the environment should prove that the type of type is Univ{n}

                TUniverse U1                        = (TUniverse)((gamma).proves( type ));

                rename( gamma );                                                            // at this point, we rename any shadowed variables, as required

                gamma                               = gamma.extend( binder, type );         // we extend the binder with the ascribed type TODO ensure that binder has structure reflecting type

                Set<Judgement<Term>> bound          = binder.decompose( gamma );            // infer the types of the symbols in the binder, from the binder's type.

                gamma                               = gamma.extend( bound );                // extend gamma with those types.

                Term T2                             = (body.type( gamma )).proves( body );  //

                System.out.println( this + " : ===> " + gamma.value() );

                TUniverse U2                        = (TUniverse)(T2.type( gamma )).proves( T2 );


                if ( U1.level >= U2.level ) {
                           gamma.unstep();
                           TAll TY = new TAll( binder, type, T2 );

                            //TODO check problems?

                           gamma.compute( this, TY );
                    return gamma.extend(  this, TY );
                } else throw new TypecheckingException( this, gamma, "Universe Inconsistency - " + U1 + " does not contain " + U2 );

            } catch ( ClassCastException _ ) {
                throw new TypecheckingException( this, gamma, "Ascription Inconsistency" );
            }

    }

    public Term reduce() throws EvaluationException {
        return new TAbstraction( binder, type, body.reduce() );
    }

    public TAbstraction rename( Environment<Term> gamma ) {
        Set<Symbol> names = binder.names();
        for (Symbol name : names ) {
            if ( gamma.contains( name ) ) {
                Symbol f = gamma.freshname( name.name()+"'" );
                binder = (Pattern)binder.substitute( f, name );
                body = body.substitute( f, name );
            }
        }
        return this;
    }


    @Override
    public Term substitute( Term x, Symbol y ) {
        if ( !binder.binds( y ) ) {
            return new TAbstraction( binder, type.substitute(x,y), body.substitute(x,y) );
        } else {
            return new TAbstraction( binder, type.substitute(x,y), body );
        }
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public Set<Symbol> free() {
        Set<Symbol> bound = binder.free();
        Set<Symbol> free = body.free();
        return Sets.difference( free, bound );
    }

    @Override
    public int hashCode() {

            int a   = binder.hashCode();
            int b   = type.hashCode();
            int c   = body.hashCode();

            return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32))) + (c ^ (c >>> 32)) + 1;

    }

}
