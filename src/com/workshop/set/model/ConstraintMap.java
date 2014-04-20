package com.workshop.set.model;

import com.workshop.set.model.interfaces.Constraint;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.lang.core.TNameGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

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
    }

    private Constraint rule;
    private Map<Symbol,Symbol> judgements;
    private Map<Symbol,Mutable<Double>> values;

    /**
     * constraining geometry with respect to a specific judgement means creating a symbolic
     * equation, and maintaining the current values of the vectors ( as synchronized, boxed mutable references. )
     *
     * @param lhs the left hand side of the binary relation
     * @param rhs the right hand side of the binary relation
     * @return this constraint map;
     */
    public ConstraintMap constrain( VectorSpace.Geometry lhs, VectorSpace.Geometry rhs ) throws VectorSpace.GeometricFailure {
        values.putAll( lhs.values() );
        values.putAll( rhs.values() );

        judgements.putAll( lhs.join( rhs ) );

        return this;
    }

    /**
     * @return the number of unknowns in this constraint map
     */
    public int arity() { return values.size(); }

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

            //cn.constrain( a, b );
            cn.constrain( ab,uv );

            System.out.println( cn );

        } catch ( VectorSpace.GeometricFailure exn ) {
            System.err.println( "ERROR: " + exn.getLocalizedMessage() );
        }


    }


}
