package com.workshop.set.model.interfaces;

import java.util.Set;

import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Pattern extends Term {
    public boolean binds( Symbol n );
    public Set<Judgement<Term>> decompose( Environment<Term> gamma ) throws TypecheckingException, ProofFailureException;
    public Set<Symbol> names();
}
