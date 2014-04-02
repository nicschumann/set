package test;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Term;
import com.workshop.set.lang.core.*;
import com.workshop.set.lang.engines.TypeUnifier;
import com.workshop.set.lang.engines.Typechecker;
import com.workshop.set.lang.exceptions.TypecheckingException;

import java.util.Arrays;
import java.util.Vector;

/**
 * Created by nicschumann on 3/30/14.
 */
public class TypeTester {
    public static class UnitTest {
        public UnitTest( Typechecker t ) {
            tc = t;
        }

        private String m = "\t\t\t";

        private Typechecker tc;

        public UnitTest header( String s ) {
            System.out.println( System.lineSeparator() + "===" + s + "===" + System.lineSeparator() );
            return this;
        }

        public UnitTest reflexivity( Term a, Term b ) {
            try {
                //System.out.println("Trying to type " + a + ", expecting " + b );
                Context c = tc.type(a);
                System.out.println( "Trying " + a + m + "\n==> " + a + " : " + c.proves( a ) +" [" + c.proves( a ).equals( b ) + "]"  );
            } catch ( TypecheckingException e ) {
                System.out.println("Failure, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } finally {
                //System.out.print( System.lineSeparator() );
                return this;
            }
        }

        public UnitTest error( Term a ) {
            try {
                System.out.println("Trying " + a + ", expecting error :");
                System.out.println( m + "Failure, returned " + a + " : " + tc.type( a ).proves( a ) );
            } catch ( TypecheckingException e ) {
                System.out.println("Success, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } finally {
                //System.out.print( System.lineSeparator() );
                return this;
            }
        }

        public UnitTest trial( Term a ) {
            try {
                System.out.println( "Trying " + a + "," );
                Context c = tc.type( a );
                System.out.println( a + " : " + c.proves(a) );
                System.out.println( "In Context : " + c );
            } catch ( TypecheckingException e ) {
                System.out.println( e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
                return this;
            }
        }
    }


    public static void main( String[] args ) {
        TNameGenerator g = new TNameGenerator();

        Typechecker t = new Typechecker();
        TypeUnifier HM = new TypeUnifier( g );
        UnitTest u = new UnitTest( t );


        TNameGenerator.TName a = g.generate("a");
        TNameGenerator.TName b = g.generate("b");
        TNameGenerator.TName c = g.generate("c");
        TNameGenerator.TName d = g.generate("d");
        TNameGenerator.TName e = g.generate("e");

        Term field = new TField();
        Term univ0 = new TUniverse( 0l );
        Term scalar = new TScalar(4.5);
        Term idA = new TAbstraction( a,new TField(),a );
        Term idB = new TAbstraction( a,new TScalar(1.0),a );
        Term judge = new TJudgement( a, b);
        Term eq = new TAbstraction( a,field,new TAbstraction( b,field,judge ) );
        Term dep = new TAbstraction( a,univ0,new TAbstraction(b,a,b));

        Term idA_app = new TApplication( idA, new TScalar( 1.0 ) );
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

            // TESTS

             u
             .header("Initial Typechecking Tests")
             .trial(field)
             .trial(univ0)
             .trial(scalar)
             .trial( idA )
             .trial( eq )
             .trial( dep )
             .trial( idA_app )
             .trial( eq_app_h )
             .trial( eq_app_f )
             .trial( dep_app )
             .trial( dep_app_f )
             .error(dep_app_bot)
             .header("Pattern Checking Tests")
             .trial(pat1)
             .trial( pat2 )
             .trial( pat3 )
             .trial(pat5)
             .error(pat4)
             .header("Pattern-Based Lambdas")
             .trial( lam1 )
             .trial( lam1_app )
             .trial( lam2 )
             .trial( lam3 )
             .trial( lam4 )
             .trial( lam4_app )
             .trial( shadow )
             .trial( shadow_app )
             .trial( shadow_app_app );
//             .error( lam4_univerr )
//             .error(lam2_err)
//             .error( lam3_bot );




    }
}
