package com.workshop.set.lang.engines;

import com.workshop.set.interfaces.Term;
import com.workshop.set.interfaces.Context;
import com.workshop.set.lang.core.TNameGenerator;
import com.workshop.set.lang.environments.EmptyTyping;
import com.workshop.set.lang.exceptions.TypecheckingException;

/**
 * Created by nicschumann on 3/30/14.
 */
public class Typechecker {
    public Typechecker() {
        this.g = new TNameGenerator();
    }

    private TNameGenerator g;

    public Context type( Term term )
        throws TypecheckingException {
        return term.type( new EmptyTyping( g ) );
    }

    public Context type( Term term, Context gamma )
        throws TypecheckingException {
        return term.type( gamma );
    }

}
