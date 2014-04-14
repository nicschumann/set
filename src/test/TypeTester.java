package test;


import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.core.*;
import com.workshop.set.model.lang.engines.Decide;
import com.workshop.set.model.lang.engines.Typechecker;
import com.workshop.set.model.lang.exceptions.EvaluationException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

/**
 * Created by nicschumann on 3/30/14.
 */
public class TypeTester {
    public static class EvalUnitTest {
        public EvalUnitTest( Typechecker t ) {
            tc = t;
        }

        private String m = "\t\t\t";

        private Typechecker tc;

        public EvalUnitTest title( String s ) {
            System.out.println("=> " + s + " <= " + System.lineSeparator() );
            return this;
        }
         public EvalUnitTest header( String s ) {
            System.out.println( System.lineSeparator() + "===| " + s + " |===" + System.lineSeparator() + System.lineSeparator() );
            return this;
        }

        public EvalUnitTest reflexivity( Term a, Term b ) {
            try {
                //System.out.println("Trying to type " + a + ", expecting " + b );
                Environment<Term> c = tc.type(a);

                System.out.println( "Trying " + a + m + "\n==> " + a + " : " + c.proves( a ) +" [" + c.proves( a ).equals( b ) + "]"  );
            } catch ( TypecheckingException e ) {
                System.out.println("Failure, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } catch ( ProofFailureException e ) {
                System.out.println("Failure, Caught ProofFailureException: ");
                System.out.println( e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
            }
            return this;
        }

        public EvalUnitTest error( Term a ) {
            try {
                System.out.println("Trying " + a + ", expecting error :");
                System.out.println( m + "Failure, returned " + a + " : " + tc.type( a ).proves( a ) );
            } catch ( TypecheckingException e ) {
                System.out.println("Success, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } catch ( ProofFailureException e ) {
                System.out.println("Success, Caught ProofFailureException: ");
                System.out.println( e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
            }
            return this;
        }

        public EvalUnitTest trial( Term a ) {
            try {
                Environment<Term> c = tc.type( a );
                String term = a.toString() + " : " + c.proves( a ).toString();
                String value = c.value().toString() + " : " + c.proves( c.value() ).toString();

                System.out.println( "\t" + term );
                System.out.println( generateInferenceLine( Math.max( term.length(), value.length() ), "beta" ) );
                System.out.println( "\t" + value );
                System.out.println( "\nEvaluation Sequence : \n" + c.evaluation() );
            } catch ( TypecheckingException e ) {
                System.out.println( e.getLocalizedMessage() );
            } catch ( ProofFailureException e ) {
                System.out.println( e.getLocalizedMessage() );
            } catch ( ArrayIndexOutOfBoundsException e ) {
                System.out.println( "ORDERING ERROR: " + e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
            }
            return this;
        }

        private String generateInferenceLine( int length, String rule ) {
            StringBuilder line = new StringBuilder();
            for ( int i = 0; i < length + 8; i++ ){
                line.append( "-" );
            }
            return line.append( " ("+rule+") " ).toString();
        }
    }

    public static class TypeUnitTest {
        public TypeUnitTest( Typechecker t ) {
            tc = t;
        }

        private String m = "\t\t\t";

        private Typechecker tc;

        public TypeUnitTest header( String s ) {
            System.out.println( System.lineSeparator() + "===| " + s + " |===" + System.lineSeparator() );
            return this;
        }

        public TypeUnitTest reflexivity( Term a, Term b ) {
            try {
                //System.out.println("Trying to type " + a + ", expecting " + b );
                Environment<Term> c = tc.type(a);

                System.out.println( "Trying " + a + m + "\n==> " + a + " : " + c.proves( a ) +" [" + c.proves( a ).equals( b ) + "]"  );
            } catch ( TypecheckingException e ) {
                System.out.println("Failure, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } catch ( ProofFailureException e ) {
                System.out.println("Failure, Caught ProofFailureException: ");
                System.out.println( e.getLocalizedMessage() );
            } finally {
                //System.out.print( System.lineSeparator() );
            }
            return this;
        }

        public TypeUnitTest error( Term a ) {
            try {
                System.out.println("Trying " + a + ", expecting error :");
                System.out.println( m + "Failure, returned " + a + " : " + tc.type( a ).proves( a ) );
            } catch ( TypecheckingException e ) {
                System.out.println("Success, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } catch ( ProofFailureException e ) {
                System.out.println("Success, Caught ProofFailureException: ");
                System.out.println( e.getLocalizedMessage() );
            } finally {
                //System.out.print( System.lineSeparator() );
            }
            return this;
        }

        public TypeUnitTest trial( Term a ) {
            try {
                System.out.println( "Trying " + a + "," );
                Environment<Term> c = tc.type( a );
                System.out.println( a + " : " + c.proves(a) );
                System.out.println( "In Context : \n" + c.typing() );
            } catch ( TypecheckingException e ) {
                System.out.println( e.getLocalizedMessage() );
            } catch ( ProofFailureException e ) {
                System.out.println( e.getLocalizedMessage() );
            } catch ( ArrayIndexOutOfBoundsException e ) {
                System.out.println( "ORDERING ERROR: " + e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
            }
            return this;
        }
    }


    public static void main( String[] args ) {
        TNameGenerator g = new TNameGenerator();

        Typechecker t = new Typechecker();
        TypeUnitTest u = new TypeUnitTest( t );
        EvalUnitTest eval = new EvalUnitTest( t );


        TNameGenerator.TName a = g.generate("a");
        TNameGenerator.TName b = g.generate("b");
        TNameGenerator.TName c = g.generate("c");
        TNameGenerator.TName d = g.generate("d");
        TNameGenerator.TName e = g.generate("e");
        TNameGenerator.TName f = g.generate("f");

        Term field = new TField();
        Term univ0 = new TUniverse( 0l );
        Term scalar = new TScalar(4.5);
        Term idA = new TAbstraction( a,new TField(),a );
        Term idB = new TAbstraction( a,new TScalar(1.0),a );
        Term judge = new TJudgement( a, b );
        Term eq = new TAbstraction( a,field,new TAbstraction( b,field,judge ) );
        Term dep = new TAbstraction( a,univ0,new TAbstraction(b,a,b));

        Term idA_app = new TApplication( idA, new TScalar( 1.0 ) );
        Term idA_app2 = new TApplication( idA, new TScalar( 2.0 ) );
        Term idA_app3 = new TApplication( idA, new TScalar( -45.2312 ) );
        Term eq_app_h = new TApplication( eq, new TScalar( 1.0 ) );
        Term eq_app_f = new TApplication( eq_app_h, new TScalar( 2.0 ) );
        Term dep_app = new TApplication( dep, field );
        Term dep_app_f = new TApplication( dep_app, scalar );
        Term dep_app_bot = new TApplication( dep_app, univ0 );


        Term pat1 = new TVector( new Vector<Term>(Arrays.asList(field,field,field)));
        Term pat2 = new TVector( new Vector<Term>(Arrays.asList(univ0,univ0,univ0)));
        Term pat3 = new TSet( new Vector<Term>(Arrays.asList(univ0,univ0,univ0)));
        Term pat4 = new TSet( new Vector<Term>(Arrays.asList(field,univ0,field)));
        Term pat5 = new TTuple( new TScalar(1.0), new TScalar(2.0) );

        Term lam1 = new TAbstraction( new TTuple( a, b ), new TSum( a, field, field ), b );
        Term lam1_app = new TApplication( lam1, new TTuple( scalar, scalar ) );

        Term lam2 = new TAbstraction(
                        new TTuple( new TVector( new Vector<Term>(Arrays.asList( a,b ) ) ),new TVector( new Vector<Term>(Arrays.asList( c,d ) ) ) ),
                        new TSum( new TVector( new Vector<Term>(Arrays.asList( a,b ) ) ), new TExponential(field,2), new TExponential( field,2 )),
                        c
                    );

        Term lam2_err = new TApplication( lam2, new TTuple( scalar, scalar ) );

        Term lam2_err2 = new TAbstraction(
                new TTuple( new TVector( new Vector<Term>(Arrays.asList( a,b ) ) ),new TVector( new Vector<Term>(Arrays.asList( c,d ) ) ) ),
                new TSum( a, field, field ),
                c
        );

        Term lam3 = new TAbstraction( new TVector( new Vector<Term>(Arrays.asList( a,b,c ))),
                                      new TExponential(field,3),
                                      new TVector( new Vector<Term>(Arrays.asList( c,b ))));

        Term lam3_bot = new TAbstraction( new TVector( new Vector<Term>(Arrays.asList( a,b,c ))),
                new TExponential(field,3),
                new TVector( new Vector<Term>(Arrays.asList( c,d ))));

        Term lam4 =
        new TAbstraction( a, univ0,
        new TAbstraction(
                    new TTuple( new TVector( new Vector<Term>( Arrays.asList( b,c ) ) ), d ),
                    new TSum( e, new TExponential( a,2 ), a ), new TSet( new Vector<Term>( Arrays.asList( b,c,d ) ) )
        ));

        Term lam4_app = new TApplication( lam4, field );
        Term lam4_univerr = new TApplication( lam4, univ0 );
        Term shadow = new TAbstraction( a, univ0, new TAbstraction( a, a, a ) );
        Term shadow_app = new TApplication( shadow, field );
        Term shadow_app_app = new TApplication( new TApplication( shadow, field ), scalar );

        // ASCRIPTION ERRORS

        Term app_err = new TAbstraction( new TTuple( a,b ), new TExponential( field, 2 ), a ); // ( a,b ) : F^2 , wrong, should be ( a,b ) : F * F
        Term app_succ = new TAbstraction( new TVector( new Vector<Term>( Arrays.asList( a, b ) ) ), new TSum( a, field, field ), a ); // ( a b ) : F * F , wrong, should be ( a b ) : F^2
        Term rank2 = new TAbstraction( f, new TAll( a, univ0, new TAll( b, a, a ) ),
                     new TAbstraction( b, univ0, new TAbstraction( c, b, new TApplication( new TApplication( f, b ), c ) )  ));

        Term rank2_app = new TApplication( rank2, dep );
        Term rank2_app2 = new TApplication( rank2_app, field );
        Term rank2_app3 = new TApplication( rank2_app2, scalar );

        Term apply = new TAbstraction( a, field, new TApplication( idA, a ) );
        Term apply2 = new TAbstraction( a, field, new TApplication( b, new TApplication( idA, scalar ) ) );
        Term apply3 = new TAbstraction( a, univ0, new TAbstraction( b, a, new TApplication( new TApplication( dep, a), b ) ) );
        //Term app_app = new TApplication( apply, scalar );

            // TESTS

            eval.title( apply.toString() ).trial(apply).title(apply2.toString()).trial( apply2 ).title(apply3.toString()).trial( apply3 )
//                    .header("Initial Typechecking Tests")
//                    .title("field typing").trial(field)
//                    .title("kind typing").trial( univ0 )
//                    .title("scalar typing").trial( scalar )
//                    .title("identity on reals").trial(idA  )
//                    .title("judgemental equality on reals").trial( eq    )
//                    .title("polymorphic identity function").trial( dep    )
//                    .title("polymorphic application").trial(
//                        new TApplication( dep, field )
//                    )
//
//
//
//                    .title("identity on reals, applied, A").trial( idA_app )
//                    .title("identity on reals, applied, B").trial( idA_app2 )
//                    .title("identity on reals, applied, C").trial( idA_app3 )
//                    .title("partially applied equality").trial( eq_app_h )
//                    .title("totally applied equality").trial( eq_app_f )
//                    .title("partially applied identity").trial( dep_app )
//                    .title("totally applied equality").trial( dep_app_f )
//
//                    .error(dep_app_bot).error(judge).error(idB)
//
//                    .header("Pattern Checking Tests")
//                    .trial(pat1)
//                    .trial( pat2 )
//                    .trial( pat3 )
//                    .trial( pat5 )
//
//                    .error(pat4)
//
//                    .header("Pattern Based Lambdas")
//                    .trial(lam1)
//                    .trial( lam1_app )
//                    .trial( lam2 )
//                    .trial( lam3 )
//                    .trial( lam4 )
//                    .trial( lam4_app )
//                    .trial( shadow )
//                    .trial( shadow_app )
//                    .trial( shadow_app_app )
//                      .error(lam4_univerr).error(lam2_err).error( lam3_bot ).error(lam2_err2)
//
//                    .error(app_err).error(app_succ)
//                     .title("RANK-2 Term").trial(rank2)
                    .title("RANK-2 Term, Application, 1").trial( rank2_app )
                    .title("RANK-2 Term, Application, 2").trial( rank2_app2 )
                    .title("RANK-2 Term, Application, 3").trial( rank2_app3 )
                    .title("APPLY-1 Term").trial(apply);
//
//                    try {
//                        System.out.print( app_app + " ==> " + app_app.reduce() );
//                    } catch ( EvaluationException v ) {
//                        System.out.print( "error" );
//                    }
//
//                    try {
//                        System.out.print( rank2_app3 + " ==> " + rank2_app3.reduce() );
//                    } catch ( EvaluationException v ) {
//                        System.out.print( "error" );
//                    }




    }
}
