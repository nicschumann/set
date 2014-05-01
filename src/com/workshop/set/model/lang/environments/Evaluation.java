package com.workshop.set.model.lang.environments;

import java.util.*;

import com.workshop.set.model.interfaces.*;
import com.workshop.set.model.lang.exceptions.EvaluationException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;

/**
 * Created by nicschumann on 4/1/14.
 */
public class Evaluation implements Environment<Term> {

    public Evaluation( Gensym g ) {

        typings = new ArrayList<Derivation<Term>>();
        evaluations = new ArrayList<Derivation<Term>>();

        valueContext = new HashMap<>();
        typeContext = new HashMap<>();

        typings.add( 0, new Derivation<Term>( g ) );
        evaluations.add( 0, new Derivation<Term>( g ) );

        deriving = 0;
        gensym = g;
    }

    private Gensym gensym;

    /* The cardinalities of the set of typings and the set of evaluations is always in bijection, and always equal to deriving
     * More formally --     forall instance : Evaluation | instance.typings.size() == instance.evaluations.size() == deriving
     * Further --           forall instance : Evaluation | forall i : N < derivation | instance.typings.get( i ).steps == instance.evaluations.get( i ).steps
     */


    private int deriving;
    private List<Derivation<Term>> typings;
    private List<Derivation<Term>> evaluations;

    private Map<Symbol,Term> valueContext;
    private Map<Symbol,Term> typeContext;

    private Term value;

    // LANG_EXTERNAL Methods
    public Term getValue( Symbol s ) throws ProofFailureException {
        if ( valueContext.containsKey(s) ) {
            return valueContext.get( s );
        } else throw new ProofFailureException( "Unbound Identifier : " + s.toString() );
    }

    public Term getType( Symbol s ) throws ProofFailureException {
        if ( valueContext.containsKey( s ) ) {
            return typeContext.get( s );
        } else throw new ProofFailureException( "Unbound Identifier : " + s.toString() );
    }

    public Environment<Term> name( Symbol s, Term t )
        throws ProofFailureException,
            TypecheckingException {

        t.type( this );
        typeContext.put( s, proves( t ) );
        valueContext.put( s, value() );
        page();

        for ( Map.Entry<Symbol,Term> VTPair : typeContext.entrySet() ) {
            extend( VTPair.getKey(), VTPair.getValue() );
        }

        return this;
    }

    // LANG_INTERNAL methods:
    // Context<Term> Methods

    /**
     *
     * This method checks to see that the Environment currently types a given term,
     * and if it does, it gives that type.
     *
     * @param t a term to retrieve the type of, in this environment;
     * @return t's type
     */
    @Override
    public Term proves( Term t )
        throws ProofFailureException {
        try {
            Derivation<Term> current = typings.get( deriving );
            return current.proves( t );
        } catch ( ArrayIndexOutOfBoundsException _ ) {
            throw new ProofFailureException( "No Recorded Derivation on this page"  );
        }
    }

    @Override
    public Term provesAt( int i, Term t )
        throws ProofFailureException {
        try {
            Derivation<Term> current = typings.get( deriving );
            return current.provesAt( i, t );
        } catch ( ArrayIndexOutOfBoundsException _ ) {
            throw new ProofFailureException( "No Recorded Derivation on this page"  );
        }
    }

    @Override
    public Environment<Term> extend( Term x, Term t ) {
        Derivation<Term> current;
        if ( typings.isEmpty() ) {
            current = new Derivation<Term>( gensym );
        } else {
            current = typings.get( deriving );
        }

        current.extend( x, t );
        typings.set(deriving, current);
        return this;
    }

    @Override
    public Environment<Term> extend( Set<Judgement<Term>> js ) {

        Derivation<Term> current;
        if ( typings.isEmpty() ) {
            current = new Derivation<Term>( gensym );
        } else {
            current = typings.get( deriving );
        }
        current.extend( js );
        typings.set(deriving, current);
        return this;
    }

    @Override
    public boolean contains( Term t ) {
        try {
            Derivation<Term> current = typings.get( deriving );
            return current.contains( t );
        } catch ( ArrayIndexOutOfBoundsException _ ) {
            return false;
        }
    }

    @Override
    public boolean wellformed() {
        try {
            Derivation<Term> current = typings.get( deriving );
            return current.wellformed();
        } catch ( ArrayIndexOutOfBoundsException _ ) {
            return false;
        }
    }

    @Override
    public Symbol freshname( String s ) { return gensym.generate( s ); }

    @Override
    public Symbol basename( String s ) { return gensym.bypass( s ); }

    @Override
    public Environment<Term> step() {
        Derivation<Term> current = typings.get( deriving );
        current.step();
        return this;
    }

    @Override
    public Environment<Term> unstep() {
        Derivation<Term> current = typings.get( deriving );
        current.unstep();
        return this;
    }

    // Environment<Term> methods

    @Override
    public Context<Term> typing() {
        return typings.get( deriving );
    }

    @Override
    public Context<Term> evaluation() {
        return evaluations.get( deriving );
    }

    /**
     * The compute function in an environment stores a step of beta reduction in
     * the current -- welltyped -- context. Note that axioms are not included in the
     * reduction - these are judgements that are always true, such as F : Univ, or univ{n} : univ{n+1}
     *
     * @param exp
     * @param type
     * @return
     */
    @Override
    public Term compute( Term exp, Term type ) throws ProofFailureException {
        try {
            Derivation<Term> eta = evaluations.get( deriving );
                eta.extend( exp.reduce(), type );
                eta.step();

            value = exp;
            return exp;

        } catch ( IndexOutOfBoundsException e ) {
            throw new ProofFailureException( "No Recorded Derivation on this page" );
        } catch ( EvaluationException e ) {
            throw new ProofFailureException( "Pattern Failure" );
        }
    }

    @Override
    public Term value() throws ProofFailureException {
        if ( value == null ) throw new ProofFailureException( "No Recorded Value in this Environment" );
        else return value;
    }

    public Evaluation page() {
        deriving += 1;
        typings.add( deriving, new Derivation<Term>( gensym ) );
        evaluations.add( deriving, new Derivation<Term>( gensym ) );
        value = null;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for ( Map.Entry<Symbol,Term> VTPair : typeContext.entrySet() ) {
            s.append( VTPair.getKey() )
             .append( " : " )
             .append( VTPair.getValue() )
             .append( "\t=>\t" )
             .append( valueContext.get( VTPair.getKey() ).toString() )
             .append( System.lineSeparator() );
        }
        return s.toString();
    }






}
