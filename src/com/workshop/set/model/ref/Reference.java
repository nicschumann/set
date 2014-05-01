package com.workshop.set.model.ref;

import com.workshop.set.model.interfaces.Symbol;

public class Reference {
    public Reference( Symbol x, MDouble y ) {
        name = x;
        value = y;
    }

    private Symbol name;
    private MDouble value;

    public Symbol getName() { return this.name; }
    public double getValue() { return this.value.get(); }
    public void setValue( double value ) {
        this.value.set( value );
    }

    @Override
    public boolean equals( Object o ) {
        try {
            return ((Reference)o).getName().equals( name )
                && ((Reference)o).getValue() == value.get();
        } catch ( ClassCastException e ) {
            return false;
        }
    }
}
