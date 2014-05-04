package com.workshop.set.model.geometry;

import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.ops.Operation;
import com.workshop.set.model.geometry.VectorSpace.*;
import com.workshop.set.model.ref.MDouble;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * an Operational Geometry represents a stack machine that computes one side of an equality.
 */
public class OperationalGeometry {
    public static abstract class Action {
        public Geometry act(Geometry lhs, Geometry rhs)
                throws GeometricFailure {
            Map<Symbol,MDouble> rhs_values = rhs.values();
            Map<Symbol,MDouble> lhs_values = lhs.values();
            for ( Map.Entry<Symbol,Symbol> tuple : lhs.join( rhs ).entrySet() ) {
                MDouble this_rhs_value = rhs_values.get( tuple.getValue() );
                this_rhs_value.set( action( lhs_values.get( tuple.getKey() ).get(), this_rhs_value.get()) );
            }
            return rhs;
        }

        public abstract double action( double lhs, double rhs );
    }


    public OperationalGeometry( Evaluation environment, Map<Operation,Action> actions ) {
        this.environment = environment;
        this.actions = actions;

        operations = new Stack<>();
        geometries = new Stack<>();

    }

    public OperationalGeometry( Evaluation environment ) {
        this.actions = new HashMap<>();
        actions.put( Operation.ADD,
            new Action() { public double action( double lhs, double rhs ) { return lhs + rhs; } }
        );

        actions.put( Operation.MULT,
            new Action() { public double action( double lhs, double rhs ) { return lhs * rhs; } }
        );


        this.environment = environment;
        operations = new Stack<>();
        geometries = new Stack<>();
    }



    private Map<Operation,Action> actions;
    private Evaluation environment;

    private Stack<Operation> operations;
    private Stack<Geometry> geometries;

    private Stack<Operation> running_operations;
    private Stack<Geometry> running_geometries;


    public OperationalGeometry operation( Operation operation ) {
        // TODO implement
        return this;
    }

    public OperationalGeometry geometry( Geometry geometry ) {
        // TODO implement
        return this;
    }


    public Geometry run() throws ProofFailureException {
        if ( !geometries.isEmpty() ) {
            try {
            init();
            Operation operator;
            Geometry rhs = geometries.pop(); // the far rhs of the geometry / the basis of the OperationalGeometry. Order Shouldn't matter though
            while ( !running_geometries.isEmpty() ) {
                rhs = running_geometries.pop();
                if ( !running_operations.isEmpty() ) {
                    operator = running_operations.pop();
                    Geometry lhs = running_geometries.pop();
                    Action a = actions.get( operator );
                    rhs = a.act( lhs, rhs );
                } else throw new ProofFailureException( "INTERNAL: Mismatched Operator and Geometry Tables" );
            }

            flush();
            return rhs;

            } catch ( GeometricFailure e ) {
                throw new ProofFailureException( "Inter" );
            }
        } else throw new ProofFailureException( "INTERNAL: This Machine Contains No Geometry." );
    }




    @SuppressWarnings("unchecked")
    private void init() {
        running_geometries      = (Stack<VectorSpace.Geometry>) geometries.clone();
        running_operations      = (Stack<Operation>) operations.clone();
    }

    private void flush() {
        running_geometries      = null;
        running_operations      = null;
    }

}
