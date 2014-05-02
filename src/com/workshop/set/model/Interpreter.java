package com.workshop.set.model;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.TNameGenerator;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.lang.exceptions.LexException;
import com.workshop.set.model.lang.exceptions.ParseException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.lang.parser.EXPRParser;
import com.workshop.set.model.lang.parser.Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Interpreter {
    public static final String prompt = "set> ";

    public Interpreter() {  running = true; }

    private boolean running;

    public static void main( String[] args ) {
        System.out.println( "Loading Set..." );
        Interpreter i = new Interpreter();
        i.loop();
    }

    public void loop() {
       TNameGenerator n = new TNameGenerator();
       BufferedReader r = new BufferedReader( new InputStreamReader( System.in ) );

       Lexer l = new Lexer();
       EXPRParser p = new EXPRParser( n );
       Evaluation env = new Evaluation( n );

       try {
           String line;

           prompt();
           while ( running && ( line = r.readLine()) != null ) {
               try {

                   handle(
                           line,
                           env,
                           l,
                           p
                   );

               } catch ( LexException e ) {
                   errln( e.getLocalizedMessage() );
               } catch ( ParseException e ) {
                   errln( e.getLocalizedMessage() );
               } catch ( ProofFailureException e ) {
                   errln( e.getLocalizedMessage() );
               } catch ( TypecheckingException e ) {
                   errln( e.getLocalizedMessage() );
               } finally {
                   if (running) prompt();
                   else outln( "done." );
               }
           }
       } catch ( IOException exn ) {
           errln( "ERROR: Caught an IOException:");
           errln( exn.getLocalizedMessage() );
           errln( System.lineSeparator() );
           loop();
       }
    }



    public Environment<Term> handle(
                String line,
                Evaluation env,
                Lexer l,
                EXPRParser p
            ) throws LexException,
               ParseException,
               ProofFailureException,
               TypecheckingException,
               IOException {

        if ( !line.isEmpty() ) {
            switch ( line.charAt( 0 ) ) {
                case ':' :
                    if ( line.contains( " " ) ) {
                        String command = line.substring(0, line.indexOf( " " ));
                        String term = line.substring( line.indexOf(" ")+1 );
                        return runCommand(
                                command, term,
                                env, l, p
                        );
                    } else {
                        return runCommand( line, "", env, l, p );
                    }
                default:
                    Term parse = p.parse( l.lex( line ) );
                    outln( env.eval( parse ).toString() );
                    return env;
            }
        } else return env;
    }


    public Environment<Term> runCommand( String command, String arg, Evaluation env, Lexer l, EXPRParser p )
       throws ParseException, LexException, IOException, ProofFailureException, TypecheckingException {
        switch ( command ) {
            case ":context":
                if ( arg.isEmpty() ) {
                    outln( env.toString() );
                    return env;
                } break;
            case ":let":
                    String symb = arg.substring(0, arg.indexOf( " " ));
                    String term = arg.substring( arg.indexOf(" ")+1 );
                    env.name( env.basename( symb ), p.parse( l.lex( term ) ) );
                    outln(env.toString());
                    return env;
            case ":type":
                    Term t = p.parse( l.lex( arg ) );
                    t.type( env );
                    outln( env.proves( t ).toString() );
                    return env;
            case ":exit":
                    running = false;
                    return env;
            default:
                errln( "Unrecognized command, \""+command+"\"");
                return env;
        }
        errln( "Unrecognized command, \""+command+"\"");
        return env;
    }

    public static boolean prompt() {
        System.out.print( prompt );
        return true;
    }

    public static void errln( String msg ) {
        System.err.println( "\u001B[31m" + msg + "\u001B[0m" );
    }
    public static void outln( String msg ) {
        System.out.println( msg );
    }
}
