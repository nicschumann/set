package com.workshop.set.lang.environments;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Judgement;
import com.workshop.set.interfaces.Term;
import com.workshop.set.lang.core.TNameGenerator;

import java.util.Map;
import java.util.Set;

/**
 * Created by nicschumann on 3/30/14.
 */
public class EmptyTyping implements Context {
    public EmptyTyping( TNameGenerator g ) {
        this.g = g;
    }

    private TNameGenerator g;

    @Override
    public boolean contains( Term t ) { return false; }

    @Override
    public Context extend( Judgement e ) { return new Typing( g ).extend( e ); }

    @Override
    public Context extend( Set<Judgement> es ) {
        return new Typing( g ).extend( es );
    }

    @Override
    public TNameGenerator.TName freshname( String s ) {
        return this.g.generate( s );
    }

    @Override
    public Term proves( Term term ) { return null; }

    @Override
    public boolean wellformed() { return true;}

    @Override
    public String toString() {
        return "[]";
    }
}
