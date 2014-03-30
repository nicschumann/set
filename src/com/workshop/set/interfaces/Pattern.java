package com.workshop.set.interfaces;

import com.workshop.set.lang.core.TNameGenerator;
import com.workshop.set.lang.judgements.HasValue;

import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public interface Pattern extends Term {
    public Set<HasValue> bind( Term value );
    public boolean binds( TNameGenerator.TName n );
    public Set<TNameGenerator.TName> names();

    @Override
    public Pattern substitute( Term x, TNameGenerator.TName y );
}
