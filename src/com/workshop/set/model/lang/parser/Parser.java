//package com.workshop.set.model.lang.parser;
//
//import java.util.*;
//
//import com.workshop.set.model.interfaces.Term;
//import com.workshop.set.model.lang.exceptions.ParseException;
//import com.workshop.set.model.lang.parser.Grammar.*;
//
///**
// * Created by nicschumann on 4/21/14.
// */
//public class Parser {
//    public Parser( NONTERMINAL accepting, Set<Production> productions ) {
//
//        this.right = new Stack<>();
//        this.left = new Stack<>();
//        this.position = 0;
//        this.ast = new ArrayList<>();
//        this.recognized = new ArrayDeque<>();
//        this.productions = productions;
//        this.accepting = accepting;
//
//        this.errors = new LinkedList<>();
//    }
//
//    private NONTERMINAL accepting;
//    private Set<Production> productions;
//
//    private Stack<TERMINAL> right;
//    private int position;
//    private Stack<TERMINAL> left;
//
//    private List<Term> ast;
//    private Deque<ITEM> recognized;
//
//
//    private List<String> errors;
//
//    /**
//     * the pushes the input onto the stack of unread tokens, sets the position pointer to the 0,
//     * pushes the accepting state onto the parse-stack, and invokes the first shift action.
//     *
//     * @param input a list of terminal symbols - tokens - to parse, typically produced by a lexing module.
//     * @return a constructed AST of the language
//     * @throws ParseException an exception, just in case parsing fails on the given input.
//     */
//    public Term parse( List<TERMINAL> input ) throws ParseException {
//        for ( int i = input.size()-1; i >= 0; i-- ) { right.push( input.get( i ) ); }
//        return shift();
//    }
//
//    /**
//     *
//     * @return
//     * @throws ParseException
//     */
//    private Term shift() throws ParseException {
//        if ( right.isEmpty() ) {
//            /* if we are at the end of the input stream,
//             * we should have constructed a complete AST, and have reduced to the accepting non-terminal symbol,
//             * we check these conditions and accept / return if they hold, and error otherwise. */
//            if ( ast.size() == 1 && recognized.size() == 1 && recognized.pop().equals( accepting )) return ast.get( 0 );
//            else throw new ParseException( ast, left, right );
//        } else {
//            /* we are in a SHIFT action and have not traversed the entire input stream yet.
//             * poll the input stream for its head and attempt to validate it against the production set. */
//            // take an image if the AST and Stack of items in its current state.
//            List<Term> imageLT = new ArrayList<>( ast );
//            Deque<ITEM> imageSI = new ArrayDeque<>( recognized );
//
//
//            // remove the first token of the inputstream, and push the current terminal onto the stack of read items.
//            int ptr = position;           // save the current position in the stream so that we can backtrack as needed.
//            for ( Production rule : productions ) { // reduce the set of productions to the set of potential matches.
//                if ( rule.matches( right.peek() ) ) { // explore this possible production state
//                    /* a match on a rule means that we have found the input terminal on the RHS of some production rule.
//                     * this means that we should execute that
//                     */
//                    try {
//                        /**
//                         * this needs fixin'.
//                         */
//                        return reduce( rule );
//                    } catch ( ParseException e ) { // this attempted reduction failed; save its error log, and test the next match
//                        errors.addAll( e.getLog() );
//                        unwind( ptr, imageLT, imageSI );
//                    }
//                }
//            }
//            /* if we have fallen out of the for loop without successfully returning a derivation, we have encountered an error
//             * throw a new ParseException, augmented with the updated error-logs, notating all the possible partial parse trees generated */
//            throw new ParseException( errors );
//        }
//    }
//
//    private Term reduce( Production rule ) throws ParseException {
//        /* we call reduce when we have matched a rule and want to explore the structure that it describes
//         * first we shift the number of terminals that we can match onto the stack of recognized items stack. */
//        for ( ITEM element : rule.expects() ) {
//            if ( element.equals( right.peek() ) ) { // if the current element matches an element of the input,
//                left.push( right.pop() ); // shift our position in the input
//                position += 1;
//            }
//            recognized.push( element ); // add the element to the recognized set of terms
//        }
//
//    }
//
//    /**
//     * This method restores the state of the automaton when a failed production rule is followed,
//     * effectively allowing the machine to backtrack and attempt a different derivation;
//     *
//     * @param frame a pointer to the automaton's position in the input stream when this step was invoked.
//     * @param astImage an image of the derived AST when this step was invoked.
//     * @param recognizedImage an image of the recognized symbols when this step was invoked.
//     */
//    private void unwind( int frame, List<Term> astImage, Deque<ITEM> recognizedImage  ) {
//        while ( position > frame ) {
//            // TODO check to make sure the indices line up properly here
//            right.push( left.pop() );
//            position--;
//        }
//        ast = new ArrayList<>( astImage );
//        recognized = new ArrayDeque<>( recognizedImage );
//    }
//
//
//}
