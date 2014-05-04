package com.workshop.set.model.run;

import com.workshop.set.model.Solver;
import com.workshop.set.model.geometry.VectorSpace;
import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Workflow;
import com.workshop.set.model.lang.core.TNameGenerator;

/**
 * Created by nicschumann on 5/4/14.
 */
public class SolverWorkflow implements Workflow<Integer,Solver> {

    public Solver run( Integer dimensionality ) {
        Gensym g = new TNameGenerator();
        VectorSpace RN = new VectorSpace( dimensionality, g );
        return new Solver( RN, g );
    }

}
