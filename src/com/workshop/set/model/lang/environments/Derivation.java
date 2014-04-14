package com.workshop.set.model.lang.environments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.workshop.set.model.interfaces.Context;
import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Judgement;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.lang.exceptions.ProofFailureException;

/**
 * The type derivation class represents the derivation of a term t's
 * type T. At each step of derivation, the evaluation
 */
public class Derivation<T> implements Context<T> {
    public Derivation( Gensym g ) {
        derivation = new ArrayList<Map<T, T>>();
        derivation.add( 0, new LinkedHashMap<T, T>());
        gensym = g;
        steps = 0;
        currentStep = 0;
    }

    private int currentStep;
    private int steps;
    private Gensym gensym;

    private List<Map<T,T>> derivation;
    private Set<T> proven;

    /**
     * Contains returns true iff the indicated term is held somewhere in the current derivation -
     * The runtime of this operation is O(steps+n), where n is max( set<judgement>.size() );
     * it performs a linear search against the derivation.
     *
     * @param t the term to check against
     * @return
     */
    @Override
    public boolean contains( T t ) {
        boolean fold = false;
        for ( Map<T,T> step : derivation ) {
            fold = step.containsKey( t );
            if ( fold ) break;
        }
        return fold;
    }

    @Override
    public Context<T> extend( T x, T T ) {
        Map<T,T> currentState = new LinkedHashMap<T, T>();
        try {
            if ( derivation.isEmpty() ) {
                currentState = new LinkedHashMap<T, T>();
                currentState.put(x, T);
                derivation.add(currentStep(), currentState);
            } else if ( !currentState.containsKey( x ) ) {
                currentState = derivation.get( currentStep() );
                currentState.put(x, T);
                derivation.set(currentStep(), currentState);
            }
            return this;
        } catch ( IndexOutOfBoundsException _ ) {
            currentState = new LinkedHashMap<T, T>();
            currentState.put( x, T );
            derivation.add(currentStep(), currentState);
            return this;
        }
    }

    @Override
    public Context<T> extend( Set<Judgement<T>> js ) {
        for ( Judgement<T> j : js ) {
            extend( j.inhabitant(), j.environment() );
        }
        return this;
    }

    @Override
    public T proves( T a )
        throws ProofFailureException {

        for ( int i = 0; i <= currentStep; i++ ) {
            Map<T,T> step = derivation.get( i );
            if ( step.containsKey( a ) ) return step.get( a );
        }

        throw new ProofFailureException( "Proof Failure in Context, blaming " + a + "\n in " + this   );
    }

    @Override
    public T provesAt( int i, T a )
        throws ProofFailureException {
        try {
            Map<T,T> step = derivation.get( i );
            if ( step.containsKey( a ) ) { return step.get( a ); }
            throw new ProofFailureException( "Proof Failure in Context, blaming " + a + "\n in " + this   );
        } catch ( IndexOutOfBoundsException _ ) {
            throw new ProofFailureException( "Proof Failure in Context, blaming " + a + "\n in " + this   );
        }
    }



    @Override
    public Symbol freshname( String s ) { return gensym.generate( s ); }


    @Override
    public Context<T> step() {
        currentStep += 1;
        if ( currentStep > steps ) {
            derivation.add( currentStep, new LinkedHashMap<T, T>() );
            steps += 1;
        }
        return this;
    }
    @Override
    public Context<T> unstep() { if ( currentStep > 0 ) currentStep -= 1; return this; }

    public int currentStep() { return currentStep; }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder( );
        int line = 0;

        if ( derivation.isEmpty() ) return "Empty Derivation";

        for ( Map<T,T> step : derivation ) {
            s.append( "[ " + line + " ] \u0393 \u22A2 " );
            for ( Map.Entry<T,T> judgement : step.entrySet() ) {
                s.append( "\t\t" + judgement.getKey() + " : " + judgement.getValue() + System.lineSeparator()  );
            }
            line += 1;
        }
        return s.toString();

    }

    @Override
    public boolean wellformed() { return true; }

}
