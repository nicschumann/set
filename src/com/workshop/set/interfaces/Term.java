package com.workshop.set.interfaces;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Term {
    /**
     * the type that this Term has with respect to the specified IContext.
     *
     * @param gamma the context to check this Term's type against
     * @return this ITerm's type in g
     */
    public Term typesAs( Context gamma );

    /**
     * the value that this Term evaluates to with respect to a specified context g
     *
     * @param eta the environment to evaluate this term in
     * @return this term's value in g
     */
    public Value evaluatesTo( Environment eta );

}
