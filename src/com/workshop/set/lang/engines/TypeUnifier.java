package com.workshop.set.lang.engines;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.core.*;
import com.workshop.set.lang.environments.EmptyTyping;
import com.workshop.set.lang.exceptions.TypecheckingException;

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
    public Term unify( TAbstraction lambda, Context gamma )
        throws TypecheckingException {
        if ( lambda.type == null ) {
            return new TAbstraction( lambda.binder, lambda.binder.type( gamma ).proves( lambda.binder ), lambda.body );
        } else return lambda;
    }







    public TypeUnifier trial( TAbstraction t ) {
            try {
                System.out.println( "Trying " + t + "," );
                System.out.println( t + " : " + unify( t, new EmptyTyping( new TNameGenerator() ) ) );
            } catch ( TypecheckingException e ) {
                System.out.println( e.getLocalizedMessage() );
            } finally {
                //System.out.print( System.lineSeparator() );
                return this;
            }
    }
}
