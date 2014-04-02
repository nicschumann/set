package com.workshop.set.lang.environments;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Gensym;
import com.workshop.set.interfaces.Judgement;
import com.workshop.set.interfaces.Term;
import com.workshop.set.lang.core.TNameGenerator;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nicschumann on 3/30/14.
 */
//public class Typing implements Context {
//    public Typing( Gensym g ) {
//        this.g = g;
//        typeset = new HashSet<Judgement>();
//        typing = new LinkedHashMap<Term, Term>();
//    }
//
//    private Gensym g;
//
//    private Set<Judgement> typeset;
//    private Map<Term,Term> typing;
//
//    @Override
//    public boolean contains( Term t ) {
//        return typing.containsKey( t );
//    }
//
//    @Override
//    public Context extend( Judgement e ) {
//
//        typeset.add( e );
//        typing.put( e.inhabitant(), e.environment() );
//
//        return this;
//    }
//
//    @Override
//    public Context extend( Set<Judgement> es ) {
//        for ( Judgement e : es ) extend( e );
//        return this;
//    }
//
//    @Override
//    public Term proves( Term term ) {
//        return typing.get( term );
//    }
//
//    @Override
//    public boolean wellformed() {
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder s = new StringBuilder( "Context |-\t");
//        for ( Map.Entry<Term,Term> e : typing.entrySet() ) {
//            s.append( "\n\t\t"+ e.getKey() + " : " + e.getValue() );
//        }
//        return s.append( System.lineSeparator() ).toString();
//    }
//
//    @Override
//    public TNameGenerator.TName freshname( String s ) {
//        return g.generate( s );
//    }
//}
