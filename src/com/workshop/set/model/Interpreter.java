package com.workshop.set.model;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.TNameGenerator;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.lang.exceptions.*;
import com.workshop.set.model.lang.parser.EXPRParser;
import com.workshop.set.model.lang.parser.Lexer;

import java.io.*;


public class Interpreter {
    public static final String prompt = "\u001B[32mset>\u001B[0m ";

    public Interpreter( InputStream in, PrintStream out, PrintStream err ) {
        dataIn = in;
        dataOut = out;
        dataErr = err;
        running = true;
    }

    private InputStream dataIn;
    private PrintStream dataOut;
    private PrintStream dataErr;

    private boolean running;

    public static void main( String[] args ) {

        System.out.println( "Loading Set..." );
        Interpreter i = new Interpreter( System.in, System.out, System.err );
        i.loop();

    }

    public void loop() {
       TNameGenerator n = new TNameGenerator();
       BufferedReader r = new BufferedReader( new InputStreamReader( dataIn ) );

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


    public Evaluation runCommand( String command, String arg, Evaluation env, Lexer l, EXPRParser p )
       throws ParseException, LexException, IOException, ProofFailureException, TypecheckingException {
        String symb;
        String term;
        switch ( command ) {
            case ":c":
            case ":context":
                if ( arg.isEmpty() ) {
                    outln( env.toString() );
                    return env;
                }
                throw new ProofFailureException( "Command \":context\" takes no arguments." );



            case ":l":
            case ":let":
                    symb = arg.substring(0, arg.indexOf( " " )).trim();
                    term = arg.substring( arg.indexOf(" ")+1 ).trim();
                    if ( symb.matches( "^[a-zA-Z0-9]*$" ) ) {
                        env.name( env.basename( symb ), p.parse( l.lex( term ) ) );
                        outln(env.toString());
                        return env;
                    }
                    throw new ProofFailureException( "Illegal Identifier: \"" + symb + "\"" );

            case ":a":
            case ":assume":
                    symb = arg.substring(0, arg.indexOf( " " )).trim();
                    term = arg.substring( arg.indexOf(" ")+1 ).trim();
                    if ( symb.matches( "^[a-zA-Z0-9]*$" ) ) {
                        Term t = p.parse( l.lex( term ) );
                        env.assume(env.basename(symb), t);
                        outln( env.toString() );
                        return env;
                    }
                    throw new ProofFailureException( "Illegal Identifier: \"" + symb + "\"" );
            case ":t":
            case ":type":
                    Term t = p.parse( l.lex( arg ) );
                    t.type( env );
                    outln( env.proves( t ).toString() );
                    return env;

            case ":s":
            case ":set":
                    symb = arg.substring(0, arg.indexOf( " " )).trim();
                    term = arg.substring( arg.indexOf(" ")+1 ).trim();
                    if ( symb.matches( "^[a-zA-Z0-9]*$" ) ) {

                        try {
                            double val = Double.parseDouble( term );
                            env.set( env.basename( symb ), val );
                            outln( env.toString() );
                            return env;
                        } catch ( NumberFormatException e ) {
                            throw new ProofFailureException( "Can't \":set\" a non-numerical component symbol: \"" + term + "\"; perhaps you meant to \":let\"?" );
                        }
                    }
                    throw new ProofFailureException( "Illegal Identifier: \"" + symb + "\"" );


            case ":q":
            case ":quit":
            case ":exit":
                    running = false;
                    return env;
            default:
                errln( "Unrecognized command, \""+command+"\"");
                return env;
        }
    }




    public void prompt() {
        dataOut.print(prompt);
    }

    public void errln( String msg ) {
        dataErr.println("\u001B[31m" + msg + "\u001B[0m");
    }
    public void outln( String msg ) {
        dataOut.println( msg );
    }
}
