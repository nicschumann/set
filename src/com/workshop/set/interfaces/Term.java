package com.workshop.set.interfaces;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Term {

    /**
     * the type that this Term has with respect to the specified IContext.
     *
     * @param gamma the context to check this Term's type against
     * @return this term's type in gamma
     */
    public Term type( Context gamma );

    /**
     * The term that this term produces under one step of evaluation
     *
     * @param eta the environment to evaluate this term in
     * @return this term's next step in eta.
     */
    public Term step( Environment eta );

    /**
     *
     * The value that this term reduces to under full reduction.
     *
     * @param eta an environment to evaluate this term in.
     * @return this term's value in eta
     */
    public Value evaluate( Environment eta );

}
