package com.workshop.set.model.lang.engines;

import com.workshop.set.model.interfaces.*;
import com.workshop.set.model.lang.core.*;
import com.workshop.set.model.lang.environments.Evaluation;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;

/**
 * Created by nicschumann on 3/31/14.
 */
public class TypeUnifier {
    public TypeUnifier( Gensym g ) { this.g = g; }

    private Gensym g;

    /* returns a new lambda, with the type of its argument inferred.
       Rules:
                \ a . a             ==>             \x : Univ . \ a : x . a
                \( a,b ) . a        ==>             \x : Univ . \ (a,b) : x . a
     */
    public Term unify( TAbstraction lambda, Environment<Term> gamma )
        throws TypecheckingException, ProofFailureException {
        if ( lambda.type == null ) {
            return new TAbstraction( lambda.binder, lambda.binder.type( gamma ).proves( lambda.binder ), lambda.body );
        } else return lambda;
    }







    public TypeUnifier trial( TAbstraction t ) {
            try {
                System.out.println( "Trying " + t + "," );
                System.out.println( t + " : " + unify( t, new Evaluation( new TNameGenerator() ) ) );
            } catch ( TypecheckingException e ) {
                System.out.println( e.getLocalizedMessage() );
            } catch (ProofFailureException e) {
				e.printStackTrace();
			} finally {
                //System.out.print( System.lineSeparator() );
            }
            return this;
    }
}
