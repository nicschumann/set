package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator.TName;
import com.workshop.set.lang.exceptions.EvaluationException;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Term {

    /**
     * the type that this Term has with respect to the specified IContext.
     *
     * @param gamma, the context to check this Term's type against
     * @return gamma, extended with the type of this term and all of its dependencies
     * @throws com.workshop.set.lang.exceptions.TypecheckingException,
     *         in case the typechecker is unable to derive a type for this term
     */
    public Context type( Context gamma ) throws TypecheckingException;

    /**
     * The term that this term produces under one step of evaluation
     *
     * @param eta, the environment to evaluate this term in
     * @return this term's next step in eta.
     */
    public Term step( Environment eta ) throws TypecheckingException, EvaluationException;

    /**
     *
     * The value that this term reduces to under full reduction.
     *
     * @param eta, an environment to evaluate this term in.
     * @return this term's value in eta
     */
    public Value evaluate( Environment eta );

    /**
     * The term that this term produces when all instances of y
     * are replaced with x.
     *
     * @param x what term
     * @param y for which binder
     * @return the new term with y replaced by x
     */
    public Term substitute( Term x, TName y );

    /**
     * the bind routine binds a pattern to a term, if such a binding is possible.
     *
     * @param value
     * @return
     * @throws PatternMatchException
     */
    public Set<HasValue> bind( Term value ) throws PatternMatchException;

    public boolean kind( Term t );

}
