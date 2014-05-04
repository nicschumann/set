package com.workshop.set.model.run;

import com.workshop.set.model.Interpreter;
import com.workshop.set.model.Solver;
import com.workshop.set.model.geometry.VectorSpace;
import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.lang.core.TNameGenerator;

/**
 * Created by nicschumann on 5/4/14.
 */
public class Init {
    public static void main( String... args ) {
        // Setup
        // [1] : Build a unique symbol generator for this instance.
        Gensym generator = new TNameGenerator();
        // [2] : Build a parsing workflow off that generator
        ParseWorkflow stringParser = new ParseWorkflow( generator );
        // [3] : Build a vectorspace in 3 dimensions, off the same generator
        VectorSpace R3 = new VectorSpace( 3, generator );
        // [4] : Build a solving model for R3.
        Solver m = new Solver( R3, generator );

        /** Establish Views */


        Interpreter interpreter = new Interpreter( m, System.in, System.out, System.err );


        // Run all dependencies
        interpreter.enterLoop();
    }
}
