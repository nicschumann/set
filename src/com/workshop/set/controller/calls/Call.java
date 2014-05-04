package com.workshop.set.controller.calls;

import java.util.concurrent.Callable;

/**
 * Created by nicschumann on 4/16/14.
 */
public abstract class Call<T> implements Callable<T> {

    protected Model m;

    public void bind( Model m ) { if ( this.m == null ) this.m = m; }

    public T call() {
        if ( m == null ) {
            System.err.println( "Call<T>: No Model Bound to this Instance" );
            return null;
        } else {
            return request();
        }
    }

    public abstract T request();

}
