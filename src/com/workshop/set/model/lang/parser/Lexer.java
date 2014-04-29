package com.workshop.set.model.lang.parser;

import java.io.*;
import java.util.ArrayList;

import com.workshop.set.model.lang.exceptions.LexException;
import com.workshop.set.model.lang.parser.Grammar.*;


public class Lexer {



    public ArrayList<TERMINAL> lex( String input ) throws LexException,IOException {
        return _lex( new BufferedReader( new StringReader( input ) ) );
    }

    public ArrayList<TERMINAL> lex( File input ) throws LexException,IOException {
        return _lex( new BufferedReader( new FileReader( input ) ) );
    }

    private String reservedSymbols = "-=[]{}()/*+:.^,";

    private int braceCount      = 0;
    private int bracketCount    = 0;
    private int parenCount      = 0;


    private ArrayList<TERMINAL> _lex( BufferedReader inputstream ) throws LexException,IOException {

        reset();

        ArrayList<TERMINAL> tokenstream = new ArrayList<>();
        StringBuilder termBuffer = new StringBuilder();

        char current;
        int position = 0;

        while ( (current = (char)inputstream.read()) != (char)-1 ) {
            System.out.println( position );
            position++;
            if ( Character.isWhitespace(current) ) {
               if ( termBuffer.length() <= 0 ) continue;

                tokenstream.add( tokenize( termBuffer.toString(), position-termBuffer.length() ) );
                termBuffer.delete(0, termBuffer.length());

            } else if ( reservedSymbols.indexOf( current ) != -1 ) {
                if ( termBuffer.length() > 0 ) {
                    tokenstream.add( tokenize( termBuffer.toString(), position-termBuffer.length() ) );
                    termBuffer.delete(0, termBuffer.length());
                }

                tokenstream.add( tokenize( ""+current, position ) );

            } else {
                termBuffer.append( current );
            }
        }

        if ( termBuffer.length() > 0 ) { tokenstream.add( tokenize( termBuffer.toString(), position-termBuffer.length() ) ); }

        if ( bracketCount == 0 && parenCount == 0 && braceCount == 0 ) {
            return tokenstream;
        } else {
            throw new LexException( Math.abs(bracketCount), Math.abs(parenCount), Math.abs(braceCount) );
        }

    }



    private TERMINAL tokenize( String token, int position ) {
        if ( token.equals( "lambda" ) || token.equals( "function" ) ) { return new LAMBDA( position ); }
        else if ( token.equals( "product" ) || token.equals( "forall" ) ) { return new FORALL( position ); }
        else if ( token.equals( "sum" ) || token.equals( "exists" ) ) { return new SUM( position ); }

        else if ( token.equals( "R" ) || token.equals( "field" ) ) { return new FIELD( position ); }
        else if ( token.equals( "univ" ) ) { return new UNIV( position ); }

        else if ( token.equals( "[" ) ) { bracketCount += 1; return new LBRACKET( position ); }
        else if ( token.equals( "(" ) ) { parenCount += 1; return new LPAREN( position ); }
        else if ( token.equals( "{" ) ) { braceCount += 1; return new LBRACE( position ); }

        else if ( token.equals( "]" ) ) { bracketCount -= 1; return new RBRACKET( position ); }
        else if ( token.equals( ")" ) ) { parenCount -= 1; return new RPAREN( position ); }
        else if ( token.equals( "}" ) ) { braceCount -= 1; return new RBRACE( position ); }

        else if ( token.equals( ":" ) ) { return new COLON( position ); }
        else if ( token.equals( "." ) ) { return new PERIOD( position ); }
        else if ( token.equals( "," ) ) { return new COMMA( position ); }

        else if ( token.equals( "+" ) ) { return new PLUS( position ); }
        else if ( token.equals( "-" ) ) { return new MINUS( position ); }
        else if ( token.equals( "/" ) ) { return new DIV( position ); }
        else if ( token.equals( "*" ) ) { return new TIMES( position ); }
        else if ( token.equals( "=" ) ) { return new EQUALS( position ); }
        else if ( token.equals( "^" ) ) { return new CARROT( position ); }

        else {
            try {
                return new NUM( Double.parseDouble( token ), position );
            } catch ( NumberFormatException _ ) {
                return new ID( token, position );
            }
        }
    }

    private void reset() {
        parenCount = 0;
        braceCount = 0;
        braceCount = 0;
    }

    public static void main( String[] args ) {
        Lexer l = new Lexer();

        try {

            System.out.println( l.lex( ")[{[][}]()" ));

        } catch ( Exception e ) {
            System.out.print( e.getLocalizedMessage() );
        }



    }
}
