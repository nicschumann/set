package com.workshop.set.model;


import java.util.*;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.lang.core.TNameGenerator;


public class ConstraintGraph {
    private static final double MULTIPLICATIVE_IDENTITY = 1.0D;
    private static final double MULTIPLICATIVE_INVERSE = -1.0D;
    private static final double ADDITIVE_IDENTITY = 0.0D;

    private class Edge {
        public Edge( Symbol at, Mutable<Double> val, Edge orbits, Double scalar ) {
            this.scalar = scalar;
            following = orbits;
            current = val;
            this.at = at;
        }

        private boolean open = true;

        private Symbol at;
        private Mutable<Double> current;
        private Edge following;
        private double scalar;

        public synchronized boolean state() { return open; }
        public synchronized void close() { open = false; }
        public synchronized void open() { open = true; }

        public void setCurrentValue( Double value ) { if ( open ) current.set( value ); }
        public double getCurrentValue( ) { return current.get(); }

        public boolean connected() { return (following != null); }
        public Edge hop() { return following; }

        public Symbol satisfy() {

            Edge current = this;
            double fold = ADDITIVE_IDENTITY;
            while ( current.connected() ) {
                current = current.hop();
                fold += scalar * current.getCurrentValue();
                current.close();
            }
            setCurrentValue(getCurrentValue() + fold);
            close();
            return current.at;

        }

    }

    public ConstraintGraph( ConstraintSet... sets ) {
        adjacencies = HashBasedTable.create();
        values = new HashMap<>();

        for ( ConstraintSet set : sets ) {
            for ( ConstraintSet.Pivot tuple : set.getRelation() ) {
                Mutable<Double> pVal =
                        ( set.getValuation().get( tuple.pivot ) != null )
                                ? new Mutable<>( set.getValuation().get( tuple.pivot ).get() )
                                : new Mutable<>( ADDITIVE_IDENTITY );
                Edge edge = construct( new ArrayList<>( tuple.orbits ), set.getValuation() );
                if ( !tuple.orbits.isEmpty() ) {
                    adjacencies.put( tuple.pivot, tuple.orbits.get( 0 ), edge );
                }
            }
        }
    }

    // Sparse representation of the ConstraintGraph as an Adjacency Matrix


    private Table<Symbol,Symbol,Edge> adjacencies;
    private Map<Symbol,Mutable<Double>> values;

    private Edge construct( ArrayList<Symbol> orbits, Map<Symbol,Mutable<Double>> values ) {
        if ( orbits.isEmpty() ) return null;
        else {
            Symbol top = orbits.remove( 0 );
            Mutable<Double> topV = values.get( top );
            return new Edge( top, ((topV != null) ? topV : new Mutable<>(0.0D)), construct( orbits, values), MULTIPLICATIVE_INVERSE  );
        }
    }






    @Override
    public String toString() {
        return adjacencies.toString();
    }

    public static void main( String[] args ) {

        VectorSpace R3 = new VectorSpace( 3, new TNameGenerator() );
        VectorSpace.Relation lineXY = R3.relation( )
    }

}
