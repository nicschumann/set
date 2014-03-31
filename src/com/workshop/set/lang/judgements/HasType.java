package com.workshop.set.lang.judgements;

import com.workshop.set.interfaces.Judgement;
import com.workshop.set.interfaces.Term;

/**
 * Created by nicschumann on 3/29/14.
 */
public class HasType implements Judgement {
    public HasType( Term inh, Term env ) {
        inhabitant = inh;
        environment = env;
    }

    private final Term inhabitant;
    private final Term environment;

    @Override
    public boolean equals( Object o ) {
        try {
            return ((HasType)o).environment().equals( environment )
                && ((HasType)o).inhabitant().equals( inhabitant );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return inhabitant().toString() + " : " + environment().toString();
    }

    @Override
    public Term inhabitant() { return inhabitant; }

    @Override
    public Term environment() { return environment; }
}
