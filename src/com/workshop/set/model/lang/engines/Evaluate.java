package com.workshop.set.model.lang.engines;


import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.TNameGenerator;

/**
 * Created by nicschumann on 4/1/14.
 */
public class Evaluate {
    public Evaluate( TNameGenerator n ) {
        this.gensym = n;
    }

    private TNameGenerator gensym;

}
