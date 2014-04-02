package com.workshop.set.lang.environments;

import com.workshop.set.interfaces.*;

import com.workshop.set.lang.exceptions.ProofFailureException;

import java.util.*;

/**
 * Created by nicschumann on 4/1/14.
 */
public class Evaluation implements Environment<Term> {

    public Evaluation( Gensym g ) {

        typings = new ArrayList<Derivation<Term>>();
        evaluations = new ArrayList<Derivation<Term>>();

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
        Derivation<Term> current = typings.get( deriving );
        current.extend( x, t );
        return this;
    }

    @Override
    public Environment<Term> extend( Set<Judgement<Term>> js ) {
        Derivation<Term> current = typings.get( deriving );
        current.extend( js );
        return this;
    }

    @Override
    public boolean contains( Term t ) {
        Derivation<Term> current = typings.get( deriving );
        return current.contains( t );
    }

    @Override
    public boolean wellformed() {
        Derivation<Term> current = typings.get( deriving );
        return current.wellformed();
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


    public Evaluation page() { deriving += 1; return this; }






}
