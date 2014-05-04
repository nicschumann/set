package com.workshop.set.model;


import java.util.*;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.lang.core.TNameGenerator;
import com.workshop.set.model.VectorSpace.*;
import com.workshop.set.model.ref.MDouble;
import com.workshop.set.model.ref.Mutable;


//public class ConstraintGraph {
//    private static final double MULTIPLICATIVE_IDENTITY = 1.0D;
//    private static final double MULTIPLICATIVE_INVERSE = -1.0D;
//    private static final double ADDITIVE_IDENTITY = 0.0D;
//
//    private class Edge {
//        public Edge( Symbol at, MDouble val, Edge orbits, Double scalar ) {
//            this.scalar = scalar;
//            following = orbits;
//            current = val;
//            this.at = at;
//        }
//
//        private boolean open = true;
//
//        private Symbol at;
//        private Mutable<Double> current;
//        private Edge following;
//        private double scalar;
//
//        public synchronized boolean state() { return open; }
//        public synchronized void close() { open = false; }
//        public synchronized void open() { open = true; }
//
//        public void setCurrentValue( Double value ) { if ( open ) current.set( value ); }
//        public double getCurrentValue( ) { return current.get(); }
//
//        public boolean connected() { return (following != null); }
//        public Edge hop() { return following; }
//
//        public Symbol satisfy() {
//
//            Edge current = this;
//            double fold = ADDITIVE_IDENTITY;
//            while ( current.connected() ) {
//                current = current.hop();
//                fold += scalar * current.getCurrentValue();
//                current.close();
//            }
//            setCurrentValue(getCurrentValue() + fold);
//            close();
//            return current.at;
//
//        }
//
//        @Override
//        public String toString() {
//            StringBuilder s = new StringBuilder();
//            s.append( "EdgeState: ").append( state() ).append( ", follow set: " );
//            if ( following != null ) s.append( following.toString() );
//            else s.append( "empty" );
//            return s.toString();
//        }
//
//    }
//
//    public ConstraintGraph( ) {
//        adjacencies = HashBasedTable.create();
//        values = new HashMap<>();
//    }
//
//    public ConstraintGraph( ConstraintSet... sets ) {
//        adjacencies = HashBasedTable.create();
//        values = new HashMap<>();
//
//        for ( ConstraintSet set : sets ) {
//            for ( ConstraintSet.Pivot tuple : set.getRelation() ) {
//                MDouble pVal =
//                        ( set.getValuation().get( tuple.pivot ) != null )
//                                ? new MDouble( set.getValuation().get( tuple.pivot ).get() )
//                                : new MDouble( ADDITIVE_IDENTITY );
//                Edge edge = construct( new ArrayList<>( tuple.orbits ), set.getValuation() );
//                if ( !tuple.orbits.isEmpty() ) {
//                    adjacencies.put( tuple.pivot, tuple.orbits.get( 0 ), edge );
//                }
//            }
//        }
//    }
//
//    private Table<Symbol,Symbol,Edge> adjacencies;
//    private Map<Symbol,MDouble> values;
//
////    public ConstraintGraph add( ConstraintSet... sets ) {
////        for ( ConstraintSet set : sets ) {
////            for ( ConstraintSet.Pivot tuple : set.getRelation() ) {
////
////            }
////        }
////    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    private Edge construct( ArrayList<Symbol> orbits, Map<Symbol,MDouble> values ) {
//        if ( orbits.isEmpty() ) return null;
//        else {
//            Symbol top = orbits.remove( 0 );
//            MDouble topV = values.get( top );
//            return new Edge( top, ((topV != null) ? topV : new MDouble(0.0D)), construct( orbits, values), MULTIPLICATIVE_INVERSE  );
//        }
//    }
//
//    @Override
//    public String toString() { return adjacencies.toString(); }
//
//    public static void main( String[] args ) {
//        TNameGenerator n = new TNameGenerator();
//
//        try {
//            VectorSpace R3 = new VectorSpace( 3, new TNameGenerator() );
//            Relation lineXY = R3.relation( n.bypass("XY"), R3.point( n.bypass("X"),  1.0, 2.0, 3.0 ) , R3.point( n.bypass("Y"),  5.0D, 6.0, 8.0 ) );
//            Relation lineAB = R3.relation( n.bypass("AB"), R3.point( n.bypass("A"),  12.5, 23.7, 3.0 ) , R3.point( n.bypass("B"),  1.0, 2.0, 4.0 ) );
//            ConstraintSet c = new ConstraintSet( new Equality() );
//            c.constrain( lineXY, lineAB );
//            ConstraintGraph g = new ConstraintGraph( c );
//
//            System.out.println( g.toString() );
//
//        } catch ( GeometricFailure e ) {
//            System.err.println( e.getLocalizedMessage() );
//        }
//
//
//    }
//
//}
