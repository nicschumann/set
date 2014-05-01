package com.workshop.set.controller.responses;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class FutureResponse<T> implements Response<T> {
    public FutureResponse( Future<T> future ) { value = future; }

    private Future<T> value;

    public T call() {
        try {
            return value.get();
        } catch ( ExecutionException e ) {
            System.out.println( "ERROR: " + e.getLocalizedMessage() );
            return null;
        } catch ( InterruptedException e ) {
            System.out.println( "ERROR: " + e.getLocalizedMessage() );
            return null;
        }
    }

    public T value() { return call(); }
}
