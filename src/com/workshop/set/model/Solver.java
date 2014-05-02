package com.workshop.set.model;

import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.lang.environments.Evaluation;


/**
 * Created by nicschumann on 5/1/14.
 */
public class Solver implements Model {
    public Solver( Gensym generator ) {
        this.generator      = generator;
        this.environment    = new Evaluation( generator );



    }

    private Evaluation          environment;
    private Gensym              generator;







}
