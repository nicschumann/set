package com.workshop.set.lang.engines;

import com.workshop.set.interfaces.Environment;
import com.workshop.set.interfaces.Term;
import com.workshop.set.lang.core.TNameGenerator;
import com.workshop.set.lang.environments.Evaluation;
import com.workshop.set.lang.exceptions.ProofFailureException;
import com.workshop.set.lang.exceptions.TypecheckingException;

/**
 * Created by nicschumann on 3/30/14.
 */
public class Typechecker {
    public Typechecker() {
        this.g = new TNameGenerator();
    }

    private TNameGenerator g;

    public Environment<Term> type( Term term )
        throws TypecheckingException, ProofFailureException {
        return term.type( new Evaluation( g ) );
    }

    public Environment<Term> type( Term term, Environment<Term> gamma )
        throws TypecheckingException, ProofFailureException {
        return term.type( gamma );
    }

}
