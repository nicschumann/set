package com.workshop.set.model.lang.core;

import java.util.HashSet;
import java.util.Set;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.engines.Decide;
import com.workshop.set.model.lang.exceptions.EvaluationException;
import com.workshop.set.model.lang.exceptions.PatternMatchException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.lang.judgements.HasValue;

/**
 * This class represents an Exponential Type :
 *
 *    G |- a : T            n in N
 * -------------------------------------------
 *              a ^ n : Univ0
 *
 */
public class TExponential implements Term {
    public TExponential( Term base, Integer exponent ) {
        this.base   = base;
        this.exp    = exponent;
    }

    public final Term base;
    public final Integer exp; // change to scalar?

    @Override
    public String toString() {
        return base.toString() + "^" + exp.toString();
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence(this, (TExponential) o, new HashSet<Symbol>(), new HashSet<Symbol>());
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {

        gamma = base.type( gamma );
        Term t = gamma.proves( base );

               gamma.compute( this, t );
        return gamma.extend(this, t);
    }

    @Override
    public Term reduce() throws EvaluationException {
        return new TExponential( base.reduce(), exp );
    }

    @Override
    public Set<Symbol> free() {
        return base.free();
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return new TExponential( base.substitute(x,y), exp );
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {

        int a   = base.hashCode();
        int b   = exp;

        return 37 * (37 * ( (a ^ (a >>> 32))) + (b ^ (b >>> 32)));

    }

}
