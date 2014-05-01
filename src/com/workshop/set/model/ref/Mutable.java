package com.workshop.set.model.ref;

import com.workshop.set.model.interfaces.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicschumann on 4/18/14.
 */
public class Mutable<T> {
    public Mutable( T varying ) { currentValue = varying; }

    private T currentValue;

    public synchronized void set( T value ) { currentValue = value; }
    public synchronized T get() { return currentValue; }

    @Override
    public boolean equals( Object o ) {
        return currentValue.equals( o );
    }

    @Override
    public String toString() {
        return "Ref[" + currentValue.toString() + "]";
    }
}
