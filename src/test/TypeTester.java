package test;

import com.workshop.set.interfaces.Term;
import com.workshop.set.lang.core.*;
import com.workshop.set.lang.engines.Typechecker;
import com.workshop.set.lang.exceptions.TypecheckingException;

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
            System.out.println( s + System.lineSeparator() );
            return this;
        }

        public UnitTest reflexivity( Term a, Term b ) {
            try {
                //System.out.println("Trying to type " + a + ", expecting " + b );
                Term c = tc.type(a);
                System.out.println( "Trying " + a + m + "\n==> " + a + " : " + c +" [" + c.equals( b ) + "]"  );
            } catch ( TypecheckingException e ) {
                System.out.println("Failure, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
                return this;
            }
        }

        public UnitTest error( Term a ) {
            try {
                System.out.println("Trying " + a + ", expecting error :");
                System.out.println( m + "Failure, returned " + a + " : " + tc.type( a ) );
            } catch ( TypecheckingException e ) {
                System.out.println("Success, Caught TypecheckingException: ");
                System.out.print( e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
                return this;
            }
        }

        public UnitTest trial( Term a ) {
            try {
                System.out.println( "Trying " + a );
                System.out.println( a + " : " + tc.type( a ) );
            } catch ( TypecheckingException e ) {
                System.out.println( e.getLocalizedMessage() );
            } finally {
                System.out.print( System.lineSeparator() );
                return this;
            }
        }
    }


    public static void main( String[] args ) {
        Typechecker t = new Typechecker();
        UnitTest u = new UnitTest( t );


        TNameGenerator g = new TNameGenerator();

        TNameGenerator.TName a = g.generate("a");
        TNameGenerator.TName b = g.generate("b");
        TNameGenerator.TName c = g.generate("c");
        TNameGenerator.TName d = g.generate("d");

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

            // TESTS
            u.header( "Initial Typechecking Tests" )
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
             .error( dep_app_bot );



//              System.out.println(field + " : " + t.type( field ));
//            System.out.println(univ0 +" : " + t.type( univ0 ) );
//            System.out.println(scalar + " : " + t.type(scalar));
//            System.out.println(idA + " : " + t.type( idA ));
//            System.out.println(idB + " : " + t.type( idB ));


    }
}
