package com.workshop.set.interfaces;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Gensym {
    public Symbol generate();
    public Symbol generate( String hint );

}
