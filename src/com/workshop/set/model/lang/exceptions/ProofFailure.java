package com.workshop.set.model.lang.exceptions;

import com.workshop.set.model.interfaces.Context;
import com.workshop.set.model.interfaces.Term;

/**
 * Created by nicschumann on 4/2/14.
 */
//public class ProofFailure<T> {
//
//    public ProofFailure() {}
//    public class ProofFailureException extends Exception {
//        public ProofFailureException( T t, Context<T> c ) {
//            reason = t;
//            blaming = c;
//        }
//
//        public ProofFailureException( String s ) {
//            msg = s;
//        }
//
//        private T reason;
//        private Context<T> blaming;
//        private String msg = "";
//
//        @Override
//        public String getLocalizedMessage() {
//
//            StringBuilder s = new StringBuilder();
//            s.append( "Proof Failure in Context, blaming " + reason  );
//            s.append( "in " + blaming );
//
//            if ( msg.isEmpty() ) msg = s.toString();
//            return msg;
//        }
//    }
//
//    public T toss( T t, Context<T> c ) throws ProofFailureException {
//        throw new ProofFailureException( t, c );
//    }
//
//    public T generic( String s ) throws ProofFailureException {
//        throw new ProofFailureException( s );
//    }
//
//}
