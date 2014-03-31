package com.workshop.set.lang.environments;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Judgement;
import com.workshop.set.interfaces.Term;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nicschumann on 3/30/14.
 */
public class Typing implements Context {
    public Typing() {
        typeset = new HashSet<Judgement>();
        typing = new LinkedHashMap<Term, Term>();
    }

    private Set<Judgement> typeset;
    private Map<Term,Term> typing;

    @Override
    public boolean contains( Judgement e ) {
        return typeset.contains( e );
    }

    @Override
    public Context extend( Judgement e ) {
        typing.put( e.inhabitant(), e.environment() );
        typeset.add( e );
        return this;
    }

    @Override
    public Context extend( Set<Judgement> es ) {
        for ( Judgement e : es ) extend( e );
        return this;
    }

    @Override
    public Term proves( Term term ) {
        return typing.get( term );
    }

    @Override
    public boolean wellformed() {
        return true;
    }

    @Override
    public String toString() {
        return typeset.toString();
    }
}
