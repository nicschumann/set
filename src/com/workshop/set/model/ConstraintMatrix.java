package com.workshop.set.model;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.workshop.set.model.interfaces.Constraint;
import com.workshop.set.model.interfaces.Symbol;

import java.util.*;

/**
 * Created by nicschumann on 4/17/14.
 */
public class ConstraintMatrix {
    private class ConstraintKey {
        public ConstraintKey( double UID ) { this.UID = UID; }
        public final double UID;

        @Override
        public boolean equals( Object o ) {
            try {
                return ((ConstraintKey)o).UID == UID;
            } catch ( ClassCastException _ ) {
                return false;
            }
        }

        @Override
        public int hashCode() { return (int)UID >>> 31; }
    }

    private static boolean verbose = true;

    private static final double multiplicativeIdentity = 1.0D;
    private static final double multiplicativeInverse = -1.0D;
    private static final double additiveIdentity = 0.0D;

    /**
     * given a set of n > 0 constraint maps, constraint matrix constructs a table of constraints,
     * where each constraint corresponds to a supplied constraint map and unknowns may be shared across maps.
     *
     * @param maps
     */
    public ConstraintMatrix( ConstraintMap... maps ) {
        matrix = HashBasedTable.create();
        values = new HashMap<>();
        constraints = new HashMap<>();

        for ( ConstraintMap map : maps ) {
            double UIDOffset = 0.0D;
            for ( Map<Symbol,Double> rel : map.constructRows()) {
                ConstraintKey key = new ConstraintKey( map.UID() + UIDOffset );
                for ( Map.Entry<Symbol,Double> pair : rel.entrySet() ) {
                    matrix.put( key, pair.getKey(), pair.getValue() );
                }
                constraints.put(key, map);
                UIDOffset += 1.0D;
            }
            values.putAll( map.currentValues() );
        }
    }

    private Table<ConstraintKey,Symbol,Double> matrix;
    private Map<Symbol,Mutable<Double>> values;
    private Map<ConstraintKey,ConstraintMap> constraints;


    public Result evaluate() throws Result.UnsatisfiableSystem {

        Map<Symbol,Double> precondition = freeze( values );
        if ( satisfied( precondition, matrix ) ) return Result.create( precondition );

        Map<Symbol,Double> condition = solve( precondition, matrix );
        if ( !condition.isEmpty() ) {
            thaw( condition, values );
        }


        return condition.check();
    }

    private Result solve( Map<Symbol,Double> precondition, Table<ConstraintKey,Symbol,Double> problem )
        throws Result.UnsatisfiableSystem {
        Map<Symbol,Double> postcondition = new HashMap<>();
        Result result = Result.undecided();

        // presents one iteration of satisfaction
        for ( Map.Entry<ConstraintKey,Map<Symbol,Double>> constraint : problem.rowMap().entrySet() ) {

            double fold = additiveIdentity;
            Table<ConstraintKey,Symbol,Double> subproblem;

            Set<Symbol> orbitSet = new HashSet<>();
            Set<Symbol> pivotSet = constraints.get( constraint.getKey() ).orbitSet();

            for ( Map.Entry<Symbol,Double> cell : constraint.getValue().entrySet() ) {
                orbitSet.add( cell.getKey() );
                if ( postcondition.containsKey( cell.getKey() ) )
                    fold += ( postcondition.get( cell.getKey() ) * cell.getValue() );
                else
                    fold += ( precondition.get( cell.getKey() ) * cell.getValue() );
            }


            pivotSet.retainAll(orbitSet); // P : intersection (symbols /\ orbits)
            orbitSet.removeAll(pivotSet); // P^complement
            Map<Symbol,Symbol> contradictions = new HashMap<>();

            for ( Symbol orbit : orbitSet ) {   // for each orbit in the problem
                if ( postcondition.containsKey( orbit ) ) { // we've reached an already modified orbit
                    if ( postcondition.get( orbit ) != precondition.get( orbit ) - fold ) {

                        /**
                         *
                         * CONTRADICTION!
                         * ie, we've reached a state with the following structure:
                         *      with y,z as orbit
                         *
                         *      x - y   ===     e
                         *      x - z   ===     e,      with     y =/= z
                         *
                         * obviously, the only way to solve this is to somehow cause y and z to be equal. we could add
                         * a constraint that y be equal to z to the constraint set and then iterate the solution on the smaller
                         * subset of the problem. if that new sub-problem is satisfiable, then we have a solution, if not,
                         * then we're at a true contradiction, and we return UNSAT. We need a way of gaging the satisfiability of a specific
                         * subproblem.
                         *
                         */
                        subproblem = HashBasedTable.create();
                        for ( Map.Entry<ConstraintKey,Double> column : problem.columnMap().get( orbit ).entrySet() ) {
                            // slice the problem along the line of the current orbit.
                            // this will produce all of the rows that the orbit is involved in.
                            Map<Symbol,Double> row = problem.rowMap().get( column.getKey() );
                            for ( Symbol pivot : pivotSet ) {
                                if ( row.get( pivot ) != null ) {
                                    subproblem.put( column.getKey(), pivot, row.get( pivot ) );
                                }
                            }
                        }

                        try {
                            postcondition.putAll( solve(postcondition, subproblem).check() ); // the recursive invocation
                        } catch ( Result.UnsatisfiableSystem unsat ) {
                            result.join( Result.create( unsat.reportContradiction(), unsat.getLocalizedMessage() ));
                        }
                    } // otherwise we're fine, we've satisfied multiple constraints on one value
                } else {
                    double prevalue = precondition.get( orbit );
                    postcondition.put( orbit, prevalue - fold );
                }

            }
        }
        // finished the iteration.
        if ( satisfied( postcondition, problem ) ) return result.join(Result.create(postcondition));
        else return result.join( Result.create() );

    }

    /**
     * This method tests a certain solution, indicated by a mapping from symbols to doubles, against a
     * constraint matrix for membership in the matrix's nullspace. it returns true iff the solution is a member,
     * and false otherwise.
     * @param condition the solution vector to test
     * @param problem the problem to test the solution against
     * @return the result of checking
     */
    private boolean satisfied( Map<Symbol,Double> condition, Table<ConstraintKey,Symbol,Double> problem ) {
        for ( Map.Entry<ConstraintKey,Map<Symbol,Double>> row : problem.rowMap().entrySet() ) {
            double rowFold = additiveIdentity;
            for ( Map.Entry<Symbol,Double> cell : row.getValue().entrySet() ) {
                if ( condition.containsKey( cell.getKey() ) ) {
                    rowFold += condition.get( cell.getKey() ) * cell.getValue();
                }
            }
            if ( rowFold != additiveIdentity ) return false;
        }
        return true;
    }


    /**
     * This method converts a map of mutable reference cells into a map of constant values
     * @param map the current mutable map to freeze reference to.
     * @return an image of map at the time when freeze() was invoked.
     */
    private static Map<Symbol,Double> freeze( Map<Symbol,Mutable<Double>> map ) {
        Map<Symbol,Double> precondition = new HashMap<>();
        for ( Map.Entry<Symbol,Mutable<Double>> pair : map.entrySet() ) {
            precondition.put( pair.getKey(), pair.getValue().get() );
        }
        return precondition;
    }

    /**
     * This method unlocks a static, value-based map, and updates the supplied mutable map's corresponding
     * cells with the values in the the static map.
     * @param image a static map with the values to be inserted
     * @param map a mutable map to be updated in a structure-preserving manner
     * @return map, with its cells updated.
     */
    private static Map<Symbol,Mutable<Double>> thaw( Map<Symbol,Double> image, Map<Symbol,Mutable<Double>> map ) {
        for ( Map.Entry<Symbol,Double> entry : image.entrySet() ) {
            if ( map.containsKey( entry.getKey() ) ) {
                map.get( entry.getKey() ).set( entry.getValue() );
            } else {
                map.put( entry.getKey(), new Mutable<Double>( entry.getValue() ) );
            }
        }
        return map;
    }


    @Override
    public String toString() {
        if ( matrix.isEmpty() ) return "[Empty Matrix]";
        if ( !verbose ) return "[Constraint Matrix]";


        StringBuilder s = new StringBuilder("\t\t\t");

        List<Symbol> symbols = new ArrayList<>();
        for ( Symbol symbol : matrix.columnKeySet() ) {
            s.append( symbol.toString() ).append( "\t\t\t" );
            symbols.add(symbol);
        }

        s.append( System.lineSeparator() );

        for ( ConstraintKey mapKey : matrix.rowKeySet() ) {
            s.append( constraints.get( mapKey ).constraint().toString() ).append("\t\t\t");
            for ( Symbol symbol : symbols ) {
                Double value = matrix.get( mapKey, symbol );
                if ( value == null ) {
                    s.append( 0.00 ).append("\t\t\t");
                } else {
                    String med = value.toString();
                    if ( med.length() >= 8 ) { med = med.substring(0, 3).concat("..."); }
                     s.append( med ).append( "\t\t\t" );
                }
            }
            s.append( System.lineSeparator() );
        }

        return s.toString();
    }



}
