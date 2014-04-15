package com.workshop.set.model.solve.core;

import com.workshop.set.model.interfaces.Constraint;
import com.workshop.set.model.interfaces.Symbol;

import java.util.Map;

/**
 *
 */
public abstract class ConstraintGraph {
    public ConstraintGraph() {}

    public abstract ConstraintGraph constrain( Constraint c );
    public abstract ConstraintGraph constrain( Map<Symbol,Float> a );



}
