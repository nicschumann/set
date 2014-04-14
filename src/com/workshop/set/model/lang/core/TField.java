package com.workshop.set.model.lang.core;

import java.util.HashSet;
import java.util.Set;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.exceptions.PatternMatchException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.judgements.HasValue;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TField implements Term {

    @Override
    public boolean equals( Object o ) {
        return o instanceof TField;
    }

    @Override
    public String toString() {
        return "\u211D";
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException {
         TUniverse ty = new TUniverse(0L);
         return gamma.extend( this, ty );

    }

    @Override
    public Term reduce() { return new TField(); }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return this;
    }

    @Override
    public Set<Symbol> free() {
        return new HashSet<Symbol>();
    }

    @Override
    public Set<HasValue> bind( Term value ) throws PatternMatchException {
        return new HashSet<HasValue>();
    }

    @Override
    public int hashCode() {
        return 1;
    }

}
