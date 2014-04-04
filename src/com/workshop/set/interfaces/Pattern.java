package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

import java.util.List;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Pattern extends Term {
    public boolean binds( Symbol n );
    public Set<Judgement<Term>> decompose( Environment<Term> gamma ) throws TypecheckingException, ProofFailureException;
    public Set<Symbol> names();
}
