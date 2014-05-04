//package com.workshop.set.model;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.workshop.set.model.geometry.VectorSpace;
//import com.workshop.set.model.interfaces.Symbol;
//import com.workshop.set.model.ref.MDouble;
//import com.workshop.set.model.ref.Reference;
//
///**
//* Created by nicschumann on 4/29/14.
//*/
//public class ConstraintSet {
//    public class Pivot {
//        public Pivot( Symbol pivot, List<Symbol> orbits ) {
//            this.pivot = pivot;
//            this.orbits = new ArrayList<>( orbits );
//        }
//
//        public final Symbol pivot;
//        public final ArrayList<Symbol> orbits;
//
//        @Override
//        public boolean equals( Object o ) {
//            try {
//                return ((Pivot)o).pivot.equals(pivot)
//                        && ((Pivot)o).orbits.equals(orbits);
//            } catch ( ClassCastException _ ) {
//                return false;
//            }
//        }
//
//        @Override
//        public String toString() {
//            return "(" + pivot.toString() + " => " + orbits.toString() + ")";
//        }
//    }
//
//    public ConstraintSet( Constraint<Symbol,Double> rule ) {
//        this.relation = new HashSet<>();
//        this.values = new HashMap<>();
//        this.rule = rule;
//    }
//
//    private Constraint<Symbol,Double> rule;
//    private Map<Symbol,MDouble> values;
//    private Set<Pivot> relation;
//
//
//    public ConstraintSet constrain( VectorSpace.Geometry pivot, VectorSpace.Geometry orbit )
//        throws VectorSpace.GeometricFailure {
//        for ( Map.Entry<Symbol,Symbol> tuple : pivot.join( orbit ).entrySet() ) {
//            relation.add( new Pivot( tuple.getKey(), new ArrayList<>(Arrays.asList( tuple.getValue() ) ) ) );
//        }
//        values.putAll( pivot.values() );
//        values.putAll( orbit.values() );
//        return this;
//    }
//
//    public ConstraintSet constrain( Reference pivot, Reference orbit )
//        throws VectorSpace.GeometricFailure {
//        return null;
//
//    }
//
//    public Set<Pivot> getRelation() { return new HashSet<>( relation ); }
//    public Map<Symbol,MDouble> getValuation() { return new HashMap<>( values ); }
//}
