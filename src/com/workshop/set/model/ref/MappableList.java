package com.workshop.set.model.ref;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by nicschumann on 5/1/14.
 */
public class MappableList<E> extends ArrayList<E> {
    public MappableList() { super(); }
    public MappableList( Collection<? extends E> elements ) { super( elements ); }
    public MappableList( int initialCapacity ) { super( initialCapacity ); }

    private final static long serialVersionUID = 294599L;

    public <F> MappableList<F> map( Function<E,F> action ) {
        MappableList<F> result = new MappableList<>();
        for ( int i = 0; i < super.size(); i++ ) {
            result.add( action.apply( super.get( i ) ) );
        }
        return result;
    }
}
