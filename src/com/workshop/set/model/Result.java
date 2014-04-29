package com.workshop.set.model;

import com.workshop.set.model.interfaces.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicschumann on 4/26/14.
 */
public class Result {
    private enum RESULT { SAT, UNSAT, UNDEC }
    public class UnsatisfiableSystem extends Exception {
        public UnsatisfiableSystem( Map<Symbol,Symbol> contradiction, String msg ) {
            this.contradiction = contradiction;
            this.literate = msg;
        }

        private Map<Symbol,Symbol> contradiction;
        private String literate;

        public Map<Symbol,Symbol> reportContradiction() { return contradiction; }

        @Override
        public String getLocalizedMessage() {
            return getMessage();
        }

        @Override
        public String getMessage() {
            return literate;
        }
    }

    private Result( Map<Symbol,Double> solution ) {
        result = RESULT.SAT;
        this.solution = solution;
        this.contradiction = new HashMap<>();

    }

    private Result( Map<Symbol,Symbol> contradiction, String msg ) {
        result = RESULT.UNSAT;
        this.solution = new HashMap<>();
        this.contradiction = contradiction;
        this.literate = msg;
    }

    private Result() {
        this.result = RESULT.UNDEC;
        this.solution = new HashMap<>();
        this.contradiction = new HashMap<>();
    }

    public RESULT result;
    private Map<Symbol,Double> solution;

    private Map<Symbol,Symbol> contradiction;
    private String literate;


    public Result join( Result other ) {
        try {
            solution.putAll( other.check() );
            if ( result == RESULT.UNDEC ) result = RESULT.SAT;
        } catch ( UnsatisfiableSystem sys ) {
            result = RESULT.UNSAT;
            contradiction.putAll( sys.reportContradiction() );
            literate += sys.getLocalizedMessage();
        }
        return this;
    }

    public Map<Symbol,Double> check() throws UnsatisfiableSystem {
        switch ( result ) {
            case SAT:
                return solution;
            case UNSAT:
                throw new UnsatisfiableSystem( contradiction, literate );
            case UNDEC:
                return solution;
            default:
                return null;
        }
    }


    public static Result create( Map<Symbol,Double> solution ) { return new Result( solution ); }
    public static Result create( Map<Symbol,Symbol> contradiction, String msg ) { return new Result( contradiction, msg ); }
    public static Result undecided() { return new Result(); }
}
