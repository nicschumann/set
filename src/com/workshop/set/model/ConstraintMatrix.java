package com.workshop.set.model;

import com.google.common.collect.Table;

import com.workshop.set.model.interfaces.Constraint;
import com.workshop.set.model.interfaces.Symbol;

/**
 * Created by nicschumann on 4/17/14.
 */
public class ConstraintMatrix {

    /**
     * given a set of n > 0 constraint maps, constraint matrix constructs a table of constraints,
     * where each constraint corresponds to a supplied constraint map and unknowns may be shared across maps.
     *
     * @param maps
     */
    public ConstraintMatrix( ConstraintMap... maps ) {


    }

    private Table<Constraint,Symbol,Mutable<Double>> matrix;




}
