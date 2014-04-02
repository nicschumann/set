package com.workshop.set.lang.core;

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
public class TAbstraction implements Term {
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
            return ((TAbstraction)o).binder.equals( binder )
                && ((TAbstraction)o).body.equals( body );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "fn " + binder.toString() + " : " + type.toString() +" .[" + body.toString() + "]";
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
                    System.out.print( gamma );

                gamma                               = type.type( gamma );                   // type the type of the formal parameter in the current environment -
                                                                                            // at this point the environment should prove that the type of type is Univ{n}
                    System.out.println( gamma );

                TUniverse U1                        = (TUniverse)((gamma).proves( type ));

                rename( gamma );                                                            // at this point, we rename any shadowed variables, as required

                gamma                               = gamma.extend( binder, type );         // we extend the binder with the ascribed type TODO ensure that binder has structure reflecting type

                Set<Judgement<Term>> bound          = binder.decompose( gamma );            // infer the types of the symbols in the binder, from the binder's type.

                gamma                               = gamma.extend( bound );                // extend gamma with those types.

                Term T2                             = (body.type( gamma )).proves( body );  //

                TUniverse U2                        = (TUniverse)(T2.type( gamma )).proves( T2 );


                if ( U1.level >= U2.level ) {
                           gamma.unstep();
                    return gamma.extend(  this, new TAll( binder, type, T2 ) );
                } else throw new TypecheckingException( this, gamma, "Universe Inconsistency - " + U1 + " does not contain " + U2 );

            } catch ( ClassCastException _ ) {
                throw new TypecheckingException( this, gamma, "Ascription Inconsistency" );
            }

    }

    public TAbstraction rename( Environment<Term> gamma ) {
        Set<TNameGenerator.TName> names = binder.names();
        for (TNameGenerator.TName name : names ) {
            if ( gamma.contains( name ) ) {
                Symbol f = gamma.freshname( name.readable+"'" );
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
    public boolean kind( Term t ) {
        return t instanceof TAbstraction;
    }

    @Override
    public int hashCode() {

            int a   = binder.hashCode();
            int b   = type.hashCode();
            int c   = body.hashCode();

            return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32))) + (c ^ (c >>> 32)) + 1;

    }

}
