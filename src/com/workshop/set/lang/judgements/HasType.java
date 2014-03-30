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
    public Term inhabitant() { return inhabitant; }

    @Override
    public Term environment() { return environment; }
}
