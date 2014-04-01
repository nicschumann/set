package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;

import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Pattern extends Term {
    public boolean binds( TNameGenerator.TName n );
    public Set<Judgement> decompose( Context gamma ) throws TypecheckingException;
    public Set<TNameGenerator.TName> names();
}
