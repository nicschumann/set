package com.workshop.set.model;

import com.workshop.set.model.interfaces.Constraint;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Value;
import com.workshop.set.model.lang.core.TNameGenerator;

import java.util.*;

/**
 * A constraint map represents a judgement between
 */
public class ConstraintMap {
    /**
     * This instantiates a mapping from geometry to geometry wrt. a specific VectorSpace,
     * where elements of the map represent equivalences according to the Judgement. Note
     * that a ConstraintMap is not necessarily symmetric, that is, a J b does NOT imply
     * b J a, forall a,b,J. For a symmetric constraints, use SymmetricConstraintMap. This
     * relation is baseline unconstrained.
     *
     * @param rule the judgement that elements of this map must respect.
     */
    public ConstraintMap( Constraint rule ) {
        this.rule = rule;
        judgements = new LinkedHashMap<>();
        values = new LinkedHashMap<>();
        orbits = new HashSet<>();
        UID = Math.random();
    }

    private double UID;
    private Constraint rule;
    private Map<Symbol,Set<Symbol>> judgements;
    private Map<Symbol,Mutable<Double>> values;
    private Set<Symbol> orbits;

    /**
     * constraining geometry with respect to a specific judgement means creating a symbolic
     * equation, and maintaining the current values of the vectors ( as synchronized, boxed mutable references. )
     * One pivot may maintain multiple orbits.
     *
     * @param pivot the left hand side of the binary relation, which is defined to be the fixed member
     * @param orbit the right hand side of the binary relation, which is defined as the varying member
     * @return this constraint map;
     */
    public ConstraintMap constrain( VectorSpace.Geometry pivot, VectorSpace.Geometry orbit ) throws VectorSpace.GeometricFailure {
        if ( ) {

        }

        judgements.putAll( pivot.join( orbit ) );

        values.putAll( pivot.values() );
        values.putAll( orbit.values() );
        orbits.addAll( orbit.components() );

        return this;

    }

    /**
     * the constructRow builds a row for the constraint matrix
     * @return a mapping from symbols into values
     */
    public List<Map<Symbol,Double>> constructRows() {
        return rule.constructRows(judgements);
    }


    /**
     * @return the number of unknowns in this constraint map
     */
    public int arity() { return values.size(); }

    /**
     * @return returns the constraint that governs this constraint map.
     */
    public Constraint constraint() { return rule; }

    /**
     * @return the current values of the ground symbols in this constraint map.
     */
    public Map<Symbol,Mutable<Double>> currentValues() { return new HashMap<>( values ); }

    /**
     * @return the set of pivot values that this map contains
     */
    public Set<Symbol> orbitSet() { return new HashSet<>( orbits ); }

    /**
     * this method returns the constraintMap's unique identifier, used to indentify it
     * in the constraint matrix.
     * @return a unique double for this instance.
     */
    public Double UID() {
        return UID;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for ( Map.Entry<Symbol,Symbol> rel : judgements.entrySet() ) {
            s.append( rel.getKey() )
             .append( " ~ " ).append(rel.getValue())
             .append( " : " ).append( rule.toString() )
             .append(System.lineSeparator());
        }

        s.append( System.lineSeparator() );

        for ( Map.Entry<Symbol,Mutable<Double>> e : values.entrySet() ) {
            s.append( e.getKey() ).append( " := ").append( e.getValue() ).append( System.lineSeparator() );
        }

        return s.toString();
    }

    @Override
    public int hashCode() {
        return (int)UID >>> 31;
    }

    /**
     * testing method
     */
    public static void main( String[] args ) {
        try {

            TNameGenerator gensym = new TNameGenerator();
            VectorSpace R3 = new VectorSpace( 3, gensym );

            VectorSpace.Point a = R3.point( gensym.generate("X"), new Mutable<Double>(1.0),new Mutable<Double>(2.0),new Mutable<Double>(3.0) );
            VectorSpace.Point b = R3.point( gensym.generate("Y"), new Mutable<Double>(5.0),new Mutable<Double>(3.442),new Mutable<Double>(0.3) );
            VectorSpace.Point u = R3.point( gensym.generate("U"), new Mutable<Double>(1.0),new Mutable<Double>(2.0),new Mutable<Double>(3.0) );
            VectorSpace.Point v = R3.point( gensym.generate("V"), new Mutable<Double>(5.0),new Mutable<Double>(3.442),new Mutable<Double>(0.3) );

            VectorSpace.Relation ab = R3.relation( gensym.generate("XY"), a, b );
            VectorSpace.Relation uv = R3.relation( gensym.generate("UV"), u, v );


            ConstraintMap cn = new ConstraintMap( new Equality() );
            cn.constrain( ab,uv );

            ConstraintMatrix mat = new ConstraintMatrix( cn );

            System.out.println( cn );
            System.out.println( mat );

        } catch ( VectorSpace.GeometricFailure exn ) {
            System.err.println( "ERROR: " + exn.getLocalizedMessage() );
        }
    }


}
