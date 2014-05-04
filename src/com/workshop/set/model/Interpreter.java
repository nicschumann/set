package com.workshop.set.model;

import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.exceptions.*;
import com.workshop.set.model.run.ParseWorkflow;

import java.io.*;


public class Interpreter {
    public static final String prompt = "\u001B[32mset>\u001B[0m ";

    public Interpreter( Solver model,
                        ParseWorkflow p,
                        InputStream in,
                        PrintStream out,
                        PrintStream err ) {

        parser = p;
        this.model = model;

        dataIn = in;
        dataOut = out;
        dataErr = err;

        running = true;
    }

    private Solver model;
    private ParseWorkflow parser;

    private InputStream dataIn;
    private PrintStream dataOut;
    private PrintStream dataErr;

    private boolean running;

    public void loop() {
       BufferedReader r = new BufferedReader( new InputStreamReader( dataIn ) );

       try {
           String line;

           prompt();
           while ( running && ( line = r.readLine()) != null ) {
               try {

                   handle( line );

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



    public void handle(
                String line
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

                        runCommand( command, term );
                        break;
                    } else {
                        runCommand( line, "" );
                        break;
                    }
                default:
                    Term parse = parser.run( line );
                    outln( model.evaluateTerm(parse).toString() );
                    break;
            }
        }
    }


    public void runCommand( String command, String arg )
       throws ParseException, LexException, IOException, ProofFailureException, TypecheckingException {
        String symb;
        String term;
         switch ( command ) {
            case ":c":
            case ":context":
                if ( arg.isEmpty() ) {
                    outln( model.toString() );
                    return;
                }
                throw new ProofFailureException( "Command \":context\" takes no arguments." );



            case ":l":
            case ":let":
                    symb = arg.substring(0, arg.indexOf( " " )).trim();
                    term = arg.substring( arg.indexOf(" ")+1 ).trim();

                    if ( symb.matches( "^[a-zA-Z0-9]*$" ) ) {

                        model.addTerm(model.name(symb), parser.run(term));
                        outln( model.toString() );
                        return;

                    }

                    throw new ProofFailureException( "Illegal Identifier: \"" + symb + "\"" );

            case ":a":
            case ":assume":
                    symb = arg.substring(0, arg.indexOf( " " )).trim();
                    term = arg.substring( arg.indexOf(" ")+1 ).trim();

                    if ( symb.matches( "^[a-zA-Z0-9]*$" ) ) {

                        model.assumeTerm( model.name( symb ), parser.run( term ) );
                        outln( model.toString() );
                        return;
                    }

                    throw new ProofFailureException( "Illegal Identifier: \"" + symb + "\"" );
            case ":t":
            case ":type":
                    outln( model.evaluateType( parser.run( arg ) ).toString() );
                    return;

            case ":s":
            case ":set":
                    symb = arg.substring(0, arg.indexOf( " " )).trim();
                    term = arg.substring( arg.indexOf(" ")+1 ).trim();

                    if ( symb.matches( "^[a-zA-Z0-9]*$" ) ) {

                        try {
                            double val = Double.parseDouble( term );
                            model.addSymbolicValue( model.name( symb ), val );
                            outln(model.toString());
                            return;
                        } catch ( NumberFormatException e ) {
                            throw new ProofFailureException( "Can't \":set\" a non-numerical component symbol: \"" + term + "\"; perhaps you meant to \":let\"?" );
                        }
                    }
                    throw new ProofFailureException( "Illegal Identifier: \"" + symb + "\"" );


            case ":q":
            case ":quit":
            case ":exit":
                    running = false;
                    return;
             default:
                errln( "Unrecognized command, \""+command+"\"");
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
