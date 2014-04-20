package com.workshop.set.model;

import com.workshop.set.model.interfaces.Constraint;

/**
 * Created by nicschumann on 4/19/14.
 */
public class Equality implements Constraint {
    public Equality() {}

    public String toString() {
        return "equality";
    }
}
