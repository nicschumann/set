package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Pattern extends Term {
    public boolean binds( TNameGenerator.TName n );
}
