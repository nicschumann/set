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
public class TApplication implements Term {
    public TApplication( Term T1, Term T2 ) {
        implication = T1;
        argument = T2;
    }

    public final Term implication;
    public final Term argument;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((TApplication)o).implication.equals( implication )
                && ((TApplication)o).argument.equals( argument );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + implication.toString() + " " + argument.toString() + ")";
    }

    @Override
    /**
     * Application encompasses several rules: TApp-t1 says that if a term can take a step
     * of evaluation, then that term can take a step of evaluation enclosed in the body
     * of an abstraction, as the first term. TApp-t2 says that the second term may step
     * iff the first term is fully evaluated. TAbs-App says that if, under the assumption of
     * a name a with a specific type T, the term t1 evaluates to t1' and its type T1 evaluates to
     * T1', then we may assume that a lambda with the appropriately typed parameter in T, applied to
     * an argument in T, results in a new term t1' with all free instances of a substituted with b
     * in a type T1' with all instances of a substituted with b.
     *
     *         t1 -[e]-> t1'
     * --------------------------- ( TApp-t1 )
     *     t1 t2 -[e]-> t1' t2
     *
     *
     *         t2 -[e]-> t2'
     * --------------------------- ( TApp-t2 )
     *       v t2 -[e]-> v t2'
     *
     *                     t1 -[e,a:T]-> t1'     T1 -[e,a:X]-> T1'
     * ---------------------------------------------------------------------------- ( TAbs-App )
     *    ( lam a:T . t1 : all a:T . T1 ) (b : T) -[e]-> t1'[a ~> b] : T1'[a ~> b]
     *
     * @param gamma, the environment to evaluate this term in
     * @return
     */
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {

            TAll All = (TAll)(implication.type( gamma )).proves( implication );
            Term T1 = (argument.type( gamma )).proves( argument );

            if ( All.type.equals( T1 ) ) {
                Term T2 = All.body;

                try{
                    for (HasValue j : All.binder.bind( argument ) ) {
                        T2 = T2.substitute( j.environment(), j.inhabitant() );
                    }
                } catch ( PatternMatchException _ ) {
                   throw new TypecheckingException( this, gamma, "Pattern Exception - " + All.binder.toString() + " cannot bind " + argument );
                }

                return gamma.extend( this, T2 );
            } else throw new TypecheckingException( this, gamma, "\n\tIncompatible types for application: " + All.toString() + " and " + T1.toString() + "\n\t" + T1.toString() + " is not an element of " + All.type.toString() );

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma, implication.toString() + " cannot be applied" );
        }
    }


    @Override
    public Term substitute( Term x, Symbol y) {
        return new TApplication( implication.substitute(x,y), argument.substitute(x,y) );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public boolean kind( Term t ) {
        return t instanceof TApplication;
    }

    @Override
    public int hashCode() {

        int a   = implication.hashCode();
        int b   = argument.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32)));

    }
}
