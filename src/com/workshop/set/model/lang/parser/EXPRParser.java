package com.workshop.set.model.lang.parser;

import java.util.*;

import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Pattern;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.*;
import com.workshop.set.model.lang.exceptions.ParseException;
import com.workshop.set.model.lang.parser.Grammar.*;

/** The following represents a refactored grammar based on S-EXPRESSIONS, for ease of parsing / evaluation.
 *
 *  T1,T2       :=          (lambda (V : T1) T1)
 *              |           (forall (V : T1) T1)
 *              |           (exists (V : T1) T1)
 *              |           (J V1 V2)
 *              |           (T1 T2)
 *              |           Univ
 *              |           Field
 *
 *  V1,V2       :=          [ V1 V2 ... VN ]
 *              |           { V1, V2, .., VN }
 *              |           [ V1, V2 ]
 *              |           (O V1 V2)
 *              |           s
 *              |           a
 *
 *  J           :=          =
 *
 *  O           :=          +
 *              |           -
 *              |           *
 *              |           /
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class EXPRParser {
    private class VTPair {
        public VTPair( Pattern V, Term T ) { term = V; type = T; }
        public final Pattern term;
        public final Term type;
    }

    public EXPRParser( Gensym generator ) {

        this.right = new Stack<>();
        this.left = new Stack<>();
        this.position = 0;
        this.markOffset = 0;
        this.errors = new LinkedList<>();
        this.generator = generator;

    }

    private Stack<TERMINAL> right;
    private Stack<TERMINAL> left;
    private int position;
    private int markOffset;
    private List<String> errors;
    private Gensym generator;

    private Set<TERMINAL> judgements = new HashSet<>( Arrays.asList( (TERMINAL)(new EQUALS( 0 )) ) );
    private Set<TERMINAL> operators = new HashSet<>( Arrays.asList( new PLUS( 0 ), new MINUS(0), new TIMES( 0 ), new DIV( 0 ) ) );

    public Term parse( ArrayList<TERMINAL> input )
        throws ParseException {
        for ( int i = input.size() - 1; i >= 0; i-- ) right.push( input.get( i ) );
        return parseTerm();
    }

    private Term parseTerm() throws ParseException {
        TERMINAL top = consult();

        if ( top instanceof LPAREN ) {
            consume();

            TERMINAL base = consult();
            if (  base instanceof LAMBDA
               || base instanceof FORALL
               || base instanceof SUM ) {

               consume();

               VTPair arg = parseVTPair();
               Term body = parseTerm();

               top = consume();

               if ( top instanceof RPAREN ) {
                   if ( base instanceof LAMBDA ) {
                       return new TAbstraction( arg.term, arg.type, body );
                   } else if ( base instanceof FORALL ) {
                       return new TAll( arg.term, arg.type, body );
                   } else {
                       return new TSum( arg.term, arg.type, body );
                   }
               } else throw new ParseException( "Mismatched Parentheses in Term", left, right );

            } else if ( isJudgement( base ) ) {
                consume();
                Term t1 = parseTerm();
                Term t2 = parseTerm();
                if ( consume() instanceof RPAREN ) {
                    return new TJudgement( t1, t2 );
                } else throw new ParseException( "Mismatched Parentheses in Term", left, right );
            } else {
                //either an application or a pattern
                mark(); // FIGURE OUT SEQUENCING
                try {
                    Term t1 = parseTerm();
                    Term t2 = parseTerm();
                    if ( consume() instanceof  RPAREN ) {
                        return new TApplication( t1, t2 );
                    }
                } catch ( ParseException _ ) {
                    backtrack();
                    return parsePattern();
                }
            }
        } else if ( top instanceof FIELD ) {
            consume();
            return new TField();
        } else if ( top instanceof UNIV ) {
            consume();
            return new TUniverse( 0L );
        } else {
            return parsePattern();
        }

        throw new ParseException( "Fell Through Cases...", left, right );
    }


    private Pattern parsePattern() throws ParseException {
        TERMINAL top = consume();

        if ( top instanceof LBRACKET ) {

            Vector<Term> internal = new Vector<>();
            do {
                internal.add( parsePattern() );
            } while ( !((top = consult()) instanceof RBRACKET) );
            consume();
            return new TVector( internal );

        } else if ( top instanceof LBRACE ) {

            Vector<Term> internal = new Vector<>();
            do {
                internal.add( parsePattern() );
                if ( !((top = consume()) instanceof COMMA )) throw new ParseException( "Expected Comma Delimiter in Set", left, right );
            } while ( !((top = consult()) instanceof RBRACKET) );
            consume();
            return new TSet( internal );

        } else if ( top instanceof LPAREN ) {
            top = consult();
            if ( isOperator( top ) ) {
                TERMINAL op = top;
                consume();
                TScalar p1 = parseScalar();
                Pattern p2 = parsePattern();
                top = consume();
                if ( top instanceof RPAREN ) {
                    if ( op instanceof PLUS ) {
                        return new TAdditive( p1, p2 );
                    } else if ( op instanceof TIMES ) {
                        return new TMultiplicative( p1, p2 );
                    } else throw new ParseException( "Unsupported Operation: " + op, left, right );
                } else throw new ParseException( "Mismatched Parenthesis in Pattern", left, right );
            } else {
                Pattern p1 = parsePattern();
                if ( (top = consume()) instanceof COMMA ) {
                    Pattern p2 = parsePattern();
                }

            }
        } else if ( top instanceof ID ) {
            return generator.bypass( ((ID) top).value );
        } else if ( top instanceof NUM ) {
            return new TScalar( ((NUM) top).value );
        }

        throw new ParseException( "Unexpected Token: " + top, left, right );

    }

    private VTPair parseVTPair() throws ParseException {
        TERMINAL top = consume();
        if ( top instanceof LPAREN ) {
            Pattern v = parsePattern();
            top = consume();
            if ( top instanceof COLON ) {
                Term t = parseTerm();
                top = consume();
                if ( top instanceof RPAREN ) {
                    return new VTPair( v, t );
                } else throw new ParseException( "Mismatched Parentheses in Argument Pair", left, right );
            } else throw new ParseException( "Missing Colon in Argument Pair", left, right );
        } else throw new ParseException( "Invalid Argument Pair", left, right );
    }

    private TScalar parseScalar() throws ParseException {
        TERMINAL top = consume();
        if ( top instanceof NUM ) {
            return new TScalar( ((NUM) top).value );
        } else throw new ParseException( "NUM Expected, found " + top, left, right );
    }


    private void backtrack() {
        while ( markOffset > position ) {
            right.push( left.pop() );
            markOffset -= 1;
        }
    }



    private boolean isJudgement( TERMINAL t ) { return judgements.contains( t ); }
    private boolean isOperator( TERMINAL t ) { return operators.contains( t ); }

    private TERMINAL consume() throws ParseException {
        if ( right.isEmpty() ) throw new ParseException( "Unexpected End of Input.", left, right );
        left.push( right.pop() );
        markOffset += 1;
        return left.peek();
    }
    private TERMINAL consult() throws ParseException {
        if ( right.isEmpty() ) throw new ParseException( "Unexpected End of Input.", left, right );
        return right.peek();
    }

    private void mark() {
        position += markOffset;
        markOffset = 0;
    }

}
