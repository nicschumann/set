package com.workshop.set.model.interfaces;

/**
 * A toolchain is a chained computation that takes an input value and returns an output value.
 * By way of example, a parser is a toolchain from strings into Abstract Syntax Trees.
 *
 * @param <T>   the type of inputs to this toolchain
 * @param <V>   the type of outputs from this toolchain.
 */
public interface Workflow<T,V> {
    public V run( T arg ) throws Exception;
}
