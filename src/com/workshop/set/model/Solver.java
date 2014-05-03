package com.workshop.set.model;

import com.workshop.set.model.interfaces.*;

import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.VectorSpace.*;


public class Solver implements Model /*, com.workshop.set.model.interfaces.Model */  {
    public Solver( VectorSpace v, Gensym generator ) {

        this.space              = v;
        this.generator          = generator;
        this.environment        = new Evaluation( generator );


    }

    /* Basis Objects :
    *  These objects are the foundations of the solution space */

    private VectorSpace         space;
    private Evaluation          environment;
    private Gensym              generator;

    /* Maps :
     * These objects are the mappings between terms and their vector representations */


    /*
     * In order for the solver to function properly, we require a mapping from
     * Terms into geometries and geometries into terms. ( or some kind of symmetric map )
     */







    /**
     * the addGeometry adds a geometry to the set of render-able elements on the stage,
     * as well as adds the appropriate terms to the context;
     * @param g a geometry to add to the context for rendering and evaluation
     */

    public void addGeometry( Geometry g ) {

    }

    private void _addGeometry( Geometry g ) {

    }

    public void addTerm( Term t ) {

    }

    public void _addTerm( Term t ) {

    }


    public void removeGeometry( Geometry g ) {

    }

    
    public void removeGeometryAll(Geometry g ) {

    }













    private Geometry termMap( Term r ) {
        return null;
    }



}
