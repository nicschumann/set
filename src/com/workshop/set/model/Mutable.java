package com.workshop.set.model;

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
        return currentValue.toString();
    }
}
