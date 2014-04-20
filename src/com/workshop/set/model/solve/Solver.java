package com.workshop.set.model.solve;

import java.util.Collection;

import com.workshop.set.model.interfaces.Constraint;
import com.workshop.set.model.interfaces.Symbol;

public class Solver
             <V extends Symbol,
              C extends Constraint> {

    /**
     * Each solver is parameterized over a
     * @param variables
     * @param constraints
     */
    public Solver
    (
            Collection<Symbol> variables,
            Collection<Constraint> constraints
    ) {

    }




}