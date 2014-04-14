package com.workshop.set.model.lang.judgements;

import com.workshop.set.model.interfaces.Judgement;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.TNameGenerator;

/**
 * Created by nicschumann on 3/30/14.
 */
public class HasValue implements Judgement<Term> {
    public HasValue( TNameGenerator.TName n, Term t ) {
        name = n;
        term = t;
    }

    public final TNameGenerator.TName name;
    public final Term term;

    public TNameGenerator.TName inhabitant() { return name; }
    public Term environment() { return term; }

    @Override
    public String toString() {
        return name.toString() + " -> " + term.toString();
    }
}
