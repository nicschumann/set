package com.workshop.set.model.lang.engines;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.TNameGenerator;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;

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
