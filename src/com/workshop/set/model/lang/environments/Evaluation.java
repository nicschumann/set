package com.workshop.set.model.lang.environments;

import java.util.*;

import com.workshop.set.model.interfaces.*;
import com.workshop.set.model.lang.core.*;

import com.workshop.set.model.lang.exceptions.EvaluationException;
import com.workshop.set.model.lang.exceptions.PatternMatchException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.lang.judgements.HasValue;
import com.workshop.set.model.ref.MDouble;
import com.workshop.set.model.ref.MappableList;


public class Evaluation implements Environment<Term> {

    public Evaluation( Gensym g ) {

        typings = new ArrayList<>();
        evaluations = new ArrayList<>();

        valueContext = new HashMap<>();
        typeContext = new HashMap<>();
        referenceContext = new HashMap<>();

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
    private Map<Symbol,MDouble> referenceContext;

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

    public Evaluation name( Symbol s, Term t )
        throws ProofFailureException,
            TypecheckingException {
        if ( !referenceContext.containsKey( s ) ) {
            t.type( this );
            typeContext.put( s, proves( t ) );
            valueContext.put( s, evaluate( t ) );
            page();

            for ( Map.Entry<Symbol,Term> VTPair : typeContext.entrySet() ) {
                extend( VTPair.getKey(), VTPair.getValue() );
            }

            for ( Map.Entry<Symbol,MDouble> VVPair : referenceContext.entrySet() ) {
                extend( VVPair.getKey(), new TField() );
            }

            return this;
        } else throw new ProofFailureException( "Mutability Error: Attempted Redefinition of mutable \""+s+"\" as a constant name." );

    }

    public Evaluation set( Symbol s, double v )
        throws ProofFailureException {
        if ( !valueContext.containsKey( s ) ) {
            if ( referenceContext.containsKey( s ) ) {
                referenceContext.get( s ).set( v );
            } else {
                referenceContext.put( s, new MDouble( v ) );
            }
            extend( s, new TField() );
            return this;
        } else throw new ProofFailureException( "Mutability Error: Attempted Redefinition of constant \""+s+"\" as a mutable name."  );
    }

    public Evaluation set( Symbol s, MDouble v )
            throws ProofFailureException {
        if ( !valueContext.containsKey( s ) ) {
            if ( referenceContext.containsKey( s ) ) {
                referenceContext.get( s ).set( v.get() );
            } else {
                referenceContext.put( s, v );
            }
            extend( s, new TField() );
            return this;
        } else throw new ProofFailureException( "Mutability Error: Attempted Redefinition of constant \""+s+"\" as a mutable name."  );
    }

    public Evaluation assume( Symbol s, Term type )
            throws ProofFailureException,
            TypecheckingException {
        type.type( this );
        if ( proves( type ) instanceof TUniverse ) {
            Term tprim = evaluate( type );
            typeContext.put( s, tprim );
            extend( s, tprim );
            return this;
        } else throw new ProofFailureException( "Ascription Inconsistency: Tried to assume an inhabitant of a non-set value." );
    }

    public Term eval( Term t )
        throws ProofFailureException,
               TypecheckingException {
        t.type(this);
        return evaluate( t );
    }
//
//    public Term type( Term t ) {
//
//    }

    // LANG_INTERNAL methods:
    // Evaluation methods:

    /**
     * This function implements the reduction relation on terms.
     * @param t a well-typed term to reduce.
     * @return the normal form of t
     */
    public Term evaluate( Term t )
        throws ProofFailureException {
        if ( t instanceof TAbstraction ) {
            return new TAbstraction(
                    ((TAbstraction) t).binder,
                    evaluate( ((TAbstraction) t).type ),
                    evaluate( ((TAbstraction) t).body )
            );
        } else if ( t instanceof TAdditive ) {
            return new TAdditive(
                    ((TAdditive) t).scalar,
                    evaluate( ((TAdditive) t).addand )
            );
        } else if ( t instanceof TAll) {
            return new TAll(
                    ((TAll) t).binder,
                    evaluate( ((TAll) t).type ),
                    evaluate( ((TAll) t).body )
            );
        } else if ( t instanceof TApplication ) {
            Term t1 = evaluate( ((TApplication) t).implication );
            Term t2 = evaluate( ((TApplication) t).argument );
            try {
                TAbstraction abs = (TAbstraction) t1;

                try {
                    Set<HasValue> valuation = abs.binder.bind( t2 );
                    for ( HasValue pair : valuation ) {
                        abs.body = abs.body.substitute(pair.term, pair.name);
                    }
                    return abs.body;
                } catch ( PatternMatchException e ) {
                    throw new ProofFailureException( "Unreported PatternMatch error - Mistyped Decomposition" );
                }
            } catch ( ClassCastException _ ) {
                return new TApplication( t1, t2 );
            }
        } else if ( t instanceof TCollection ) {
            return new TCollection(
                    evaluate(((TCollection) t).contents ),
                    ((TCollection) t).cardinality
            );
        } else if ( t instanceof TExponential ) {
            return new TExponential(
                    evaluate( ((TExponential) t).base ),
                    ((TExponential) t).exp
            );
        } else if ( t instanceof TField ) {
            return new TField();
        } else if ( t instanceof TJudgement ) {
            return new TJudgement(
                evaluate( ((TJudgement) t).left ),
                evaluate( ((TJudgement) t).right )
            );
        } else if ( t instanceof TMultiplicative ) {
            return new TMultiplicative(
                    ((TMultiplicative) t).scalar,
                    evaluate( ((TMultiplicative) t).multiplicand )
            );
        } else if ( t instanceof TNameGenerator.TName ) {
            if ( valueContext.containsKey( t ) ) {
                return evaluate( valueContext.get( t ) );
            } else if ( referenceContext.containsKey( t ) ) {
                return new TScalar( referenceContext.get( t ) );
            } else return t;
        } else if ( t instanceof TScalar ) {
            return new TScalar( ((TScalar) t).index );
        } else if ( t instanceof TSet ) {

            MappableList<Term> m = new MappableList<>();
            for ( Term t_i : ((TSet) t).elements() ) m.add( evaluate( t_i ) );
            return new TSet( m );

        } else if ( t instanceof TSum ) {
            return new TAbstraction(
                    ((TSum) t).binder,
                    evaluate( ((TSum) t).type ),
                    evaluate( ((TSum) t).body )
            );
        } else if ( t instanceof TTuple ) {
            return new TTuple(
                evaluate( ((TTuple) t).domain ),
                evaluate( ((TTuple) t).range )
            );
        } else if ( t instanceof TUniverse ) {
            return new TUniverse( ((TUniverse) t).level );
        } else if ( t instanceof TVector ) {

            MappableList<Term> m = new MappableList<>();
            for ( Term t_i : ((TVector) t).components() ) m.add( evaluate( t_i ) );
            return new TVector( m );

        } else {

            throw new ProofFailureException( "INTERNAL: Unrecognized Term Form" );

        }
    }





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
            current = new Derivation<>( gensym );
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
            current = new Derivation<>( gensym );
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
             .append( "\t=>\t" );

             if ( valueContext.containsKey(VTPair.getKey())) {
                 s.append( valueContext.get( VTPair.getKey() ).toString() );
             } else { s.append( "assumed" ); }

             s.append( System.lineSeparator() );
        }

        if ( !referenceContext.isEmpty() ) {
            s.append( "-------------------------------------------------" )
             .append(System.lineSeparator());

            for ( Map.Entry<Symbol,MDouble> VVPair : referenceContext.entrySet() ) {
                s.append( VVPair.getKey() ).append( "\t=>\t" ).append( VVPair.getValue().get() );
                s.append( System.lineSeparator() );
            }
        }

        return s.toString();
    }






}
