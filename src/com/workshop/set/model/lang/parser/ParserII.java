package com.workshop.set.model.lang.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import com.workshop.set.model.interfaces.Pattern;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.*;
import com.workshop.set.model.lang.exceptions.RecursiveDescentParseException;
import com.workshop.set.model.lang.parser.Grammar.*;

/**
 * Created by nicschumann on 4/22/14.
 */
public class ParserII {
    public ParserII( List<TERMINAL> input ) {
        this.input = input;
        this.stackframes = new Stack<>();
        this.depth = 0;
        this.gensym = new TNameGenerator();
    }


    private TNameGenerator gensym;
    private List<TERMINAL> input;
    private Stack<List<TERMINAL>> stackframes;
    private int depth;


    public Term parse() throws RecursiveDescentParseException {
        return parseTerm();
    }

    private Term parseTerm() throws RecursiveDescentParseException {
        if ( done() ) {
            throw new RecursiveDescentParseException( new END( -1 ), input );
        } else {

            TERMINAL t = consult();
            TERMINAL base = t;
            Term T;

            // ##ABSTRACTION CASE
            if ( base instanceof LAMBDA ||
                 base instanceof FORALL ||
                 base instanceof SUM
                ) {

                consume();
                Pattern pattern = parsePattern();

                if ( ( t = consume() ) instanceof COLON ) {
                    Term type = parseTerm();
                    if ( ( t = consume() ) instanceof PERIOD ) {
                        Term body = parseTerm();
                        if ( base instanceof LAMBDA ) {
                            T = new TAbstraction( pattern, type, body );
                        } else if ( base instanceof FORALL ) {
                            T = new TAll( pattern, type, body );
                        } else {
                            T = new TSum( pattern, type, body );
                        }
                        return T;
                    }
                }

                throw new RecursiveDescentParseException( t, input );

            // ##UNIV CASE
            } else if ( base instanceof UNIV ) {
                consume();
                return new TUniverse( 0L );
            // ##FIELD CASE
            } else if ( base instanceof FIELD ) {
                consume();
                return new TField();
            // ##APPLICATION CASE
            } else if ( base instanceof LPAREN ) {

                consume();
                Term T1 = parseTerm();
                TERMINAL J;
                boolean fail = false;

                wind();

                try {
                   J = getJudgement();
                   try {
                       Term T2 = parseTerm();
                       if ( (t = consume()) instanceof RPAREN ) {
                           return new TJudgement( T1, T2 );
                       } else throw new RecursiveDescentParseException( t, input );
                   } catch ( RecursiveDescentParseException _ ) {
                       fail = true; throw new RecursiveDescentParseException( J, input );
                   }
                } catch ( RecursiveDescentParseException _ ) {
                    if ( fail ) throw new RecursiveDescentParseException( consult(), input );
                }

                unwind();

                Term T2 = parseTerm();
                if ( (t = consume()) instanceof RPAREN ) {
                    return new TApplication( T1, T2 );
                } else throw new RecursiveDescentParseException( t, input );
            } else {
                return parsePattern();
            }
        }
    }

    private Pattern parsePattern( ) throws RecursiveDescentParseException {
        TERMINAL base = consult();

        if ( base instanceof ID ) {
            TNameGenerator.TName a = gensym.bypass( ((ID) base).value );
            TERMINAL OP;
            consume();

            wind();

//            try {
//                OP = getOperator()
//            }
            // TODO FINISH THIS SHIT

            return null;
        } else if ( base instanceof NUM ) {
            //TODO handle case of an op

            return new TScalar( ((NUM) base).value );
        } else if ( base instanceof LBRACKET ) {

            consume();
            base = consult();
            wind();


            try {

                Vector<Term> internal = new Vector<>();
                while ( !(base instanceof RBRACKET) && !done() ) {
                    consume();
                    internal.add( parsePattern() );
                    base = consult();
                }

                if ( base instanceof RBRACKET ) { return new TVector( internal ); }
                else { throw new RecursiveDescentParseException( base, input ); }
            } catch ( RecursiveDescentParseException _ ) {

                unwind();

                base = consult();
                Term D = parsePattern();
                base = consult();
                if ( base instanceof COMMA ) {
                    consume();
                    Term C = parsePattern();
                    if ( (base = consume()) instanceof RBRACKET ) {
                        return new TTuple( D, C );
                    }
                }

                throw new RecursiveDescentParseException( base, input );


            }



        } else if ( base instanceof LBRACE ) {
            // TODO handle set
        }
        // TODO FINISH THIS SHIT TOO
        return null;
    }

    private TERMINAL getJudgement() throws RecursiveDescentParseException {
        TERMINAL t = consume();
        if ( t instanceof EQUALS ) {
            return t;
        } else {
            throw new RecursiveDescentParseException( t, input );
        }
    }

    private TERMINAL getOperator() throws RecursiveDescentParseException {
        TERMINAL t = consume();
        if ( t instanceof TIMES || t instanceof PLUS
          || t instanceof MINUS || t instanceof DIV ) {
            return t;
        } else {
            throw new RecursiveDescentParseException( t, input );
        }
    }


    private void wind() { depth += 1; stackframes.push( new ArrayList<TERMINAL>( input ) ); }
    private void unwind() { stackframes.push( new ArrayList<TERMINAL>( input ) ); }

    private boolean done() { return input.isEmpty(); }
    private TERMINAL consume( ) { return input.remove( 0 ); }
    private TERMINAL consult( ) { return input.get( 0 ); }
}
