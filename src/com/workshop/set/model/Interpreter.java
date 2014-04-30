package com.workshop.set.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by nicschumann on 4/27/14.
 */
public class Interpreter {
    public static final String prompt = "set> ";

    public static void main( String[] args ) {
        loop();
    }

    public static void loop() {
       BufferedReader r = new BufferedReader( new InputStreamReader( System.in ) );

       try {
           String line = null;

           prompt();
           while ( !(line = r.readLine()).isEmpty() ) {

               prompt();
           }
       } catch ( IOException exn ) {
           errln( "ERROR: Caught an IOException:");
           errln( exn.getLocalizedMessage() );
           errln( System.lineSeparator() );
           loop();
       }
    }


    /**
     * @param line
     */
    private static void handle( String line ) {
        String[] sep = line.split("\\s+");
        switch( sep.length ) {
            case 1:
                handleCommand( sep[ 0 ] );
                break;


        }
    }

    public static void prompt() {
        System.out.print( prompt );
    }

    public static void errln( String msg ) {
        System.err.println(msg);
    }
    public static void outln( String msg ) {
        System.out.println( msg );
    }
}
