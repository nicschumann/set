package com.workshop.set.model.lang.environments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.workshop.set.model.interfaces.Context;
import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Judgement;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.exceptions.EvaluationException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;

/**
 * Created by nicschumann on 4/1/14.
 */
public class Evaluation implements Environment<Term> {

    public Evaluation( Gensym g ) {

        typings = new ArrayList<Derivation<Term>>();
        evaluations = new ArrayList<Derivation<Term>>();

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

    private Term value;

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
            Term T = current.proves( t );
            return T;
        } catch ( ArrayIndexOutOfBoundsException _ ) {
            throw new ProofFailureException( "No Recorded Derivation on this page"  );
        }
    }

    @Override
    public Term provesAt( int i, Term t )
        throws ProofFailureException {
        try {
            Derivation<Term> current = typings.get( deriving );
            Term T = current.provesAt( i, t );
            return T;
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
        return this;
    }

    @Override
    public String toString() {
        Derivation<Term> t = evaluations.get( deriving );
        return t.toString();
    }






}
