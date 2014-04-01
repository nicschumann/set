package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.EvaluationException;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;
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

    @Override
    public Context type( Context gamma )
        throws TypecheckingException {

            try {
                // the type of the formal parameter to the abstraction should be univ[n]

                Context gamma1          = type.type( gamma );
               // System.out.println("====>" +gamma1 );

               // System.out.println("====>" + gamma1.proves( type ));
                TUniverse U1            = (TUniverse)((gamma1).proves( type ));

                rename( gamma1 );
                System.out.println( this );

                //System.out.println("====>" + U1 );
                // the binder implies a set of judgements inferrable from its structure and the binder's type.

                gamma1                  = gamma1.extend( new HasType( binder,type ) );
                Set<Judgement> bound    = binder.decompose( gamma1 );
                // the body should type soundly in a context extended with the decomposition of the binder.
                gamma1                  = gamma1.extend( bound );

                //System.out.println("G====>" +gamma1 );
                Term T2                 = (body.type( gamma1 )).proves( body );
                //System.out.println("====>" + T2 );
                // the body should type in univ[n+k]
                TUniverse U2            = (TUniverse)(T2.type( gamma1 )).proves( T2 );
                //System.out.println("====>" + U2 );

                  if ( U1.level >= U2.level ) {
                    return gamma1.extend( new HasType( this, new TAll( binder, type, T2 ) ) );
                } else throw new TypecheckingException( this, gamma, "Universe Inconsistency - " + U1 + " does not contain " + U2 );

            } catch ( ClassCastException _ ) {
                throw new TypecheckingException( this, gamma, "Ascription Inconsistency" );
            }

    }

    public TAbstraction rename( Context gamma ) {
        Set<TNameGenerator.TName> names = binder.names();
        for (TNameGenerator.TName name : names ) {
            if ( gamma.contains( name ) ) {
                TNameGenerator.TName f = gamma.freshname( name.readable+"'" );
                binder = (Pattern)binder.substitute( f, name );
                body = body.substitute( f, name );
            }
        }
        return this;
    }


    /**
     * A TAbstraction takes a step of evaluation by evaluating the terms inside its body,
     * with the assumption of its parameter have a certain type, while maintaining the structure
     * of its abstraction:
     *
     *                     t -[e]-> t'
     * -------------------------------------------------------        (TAbs)
     *    lambda x : T . t   -[e]->   lambda x : T . t'
     *
     *
     * @param eta, the environment to evaluate this term in
     * @return the term t' that this term t steps to under reduction.
     */
    @Override
    public Term step( Environment eta ) {
//        throws TypecheckingException, EvaluationException {
//        // 1: Extend eta with the type of this term.
//        Environment etaPrime = eta.extend( new HasType( this, this.type( eta.typing() ) ) );
//        // 2: Step the body of the abstraction, in the new eta.
//        return new TAbstraction( binder, type, body.step( etaPrime ) );
            return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        // TODO : define big step evaluation
        return null;
    }

    @Override
    public Term substitute( Term x, TNameGenerator.TName y ) {
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
