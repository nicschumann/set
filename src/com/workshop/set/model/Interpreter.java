package com.workshop.set.model;

import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.TNameGenerator;
import com.workshop.set.model.lang.exceptions.LexException;
import com.workshop.set.model.lang.exceptions.ParseException;
import com.workshop.set.model.lang.parser.EXPRParser;
import com.workshop.set.model.lang.parser.Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Interpreter {
    public static final String prompt = "set> ";

    public static void main( String[] args ) {
        System.out.println( "Loading Set..." );
        prompt();
        loop();
    }

    public static void loop() {
       TNameGenerator n = new TNameGenerator();
       BufferedReader r = new BufferedReader( new InputStreamReader( System.in ) );
       Lexer l = new Lexer();
       EXPRParser p = new EXPRParser( n );

       try {
           String line;

           while ( ( line = r.readLine()) != null ) {
               try {
                   Term parse = p.parse( l.lex( line ) );
                   outln( parse.toString() );
               } catch ( LexException e ) {
                   errln( e.getLocalizedMessage() );
               } catch ( ParseException e ) {
                   errln( e.getLocalizedMessage() );
               } finally {
                   prompt();
               }
           }
       } catch ( IOException exn ) {
           errln( "ERROR: Caught an IOException:");
           errln( exn.getLocalizedMessage() );
           errln( System.lineSeparator() );
           loop();
       }
    }

    public static boolean prompt() {
        System.out.print( prompt );
        return true;
    }

    public static void errln( String msg ) {
        System.err.println(msg);
    }
    public static void outln( String msg ) {
        System.out.println( msg );
    }
}
