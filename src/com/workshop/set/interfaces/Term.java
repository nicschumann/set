package com.workshop.set.interfaces;


import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import com.workshop.set.lang.judgements.HasValue;

import java.util.List;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Term {

    //public boolean alpha_equivalent( Term t2, Set<Symbol> g1, Set<Symbol> g2 );
    /**
     * the type that this Term has with respect to the specified IContext.
     *
     * @param gamma, the context to check this Term's type against
     * @return gamma, extended with the type of this term and all of its dependencies
     * @throws com.workshop.set.lang.exceptions.TypecheckingException,
     *         in case the typechecker is unable to derive a type for this term
     */
    public Environment<Term> type( Environment<Term> gamma ) throws TypecheckingException, ProofFailureException;

    /**
     * The term that this term produces when all instances of y
     * are replaced with x.
     *
     * @param x what term
     * @param y for which binder
     * @return the new term with y replaced by x
     */
   public Term substitute( Term x, Symbol y );

    /**
     * the bind routine binds a pattern to a term, if such a binding is possible.
     *
     * @param value
     * @return
     * @throws PatternMatchException
     */
    public Set<HasValue> bind( Term value ) throws PatternMatchException;

    public Set<Symbol> free();



}
