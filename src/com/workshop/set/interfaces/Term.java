package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator.TName;
import com.workshop.set.lang.exceptions.TypecheckingException;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Term {

    /**
     * the type that this Term has with respect to the specified IContext.
     *
     * @param gamma, the context to check this Term's type against
     * @return this term's type in gamma
     * @throws com.workshop.set.lang.exceptions.TypecheckingException,
     *         in case the typechecker is unable to derive a type for this term
     */
    public Term type( Context gamma ) throws TypecheckingException;

    /**
     * The term that this term produces under one step of evaluation
     *
     * @param eta, the environment to evaluate this term in
     * @return this term's next step in eta.
     */
    public Term step( Environment eta );

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

}
