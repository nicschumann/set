package com.workshop.set.model.lang.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Judgement;
import com.workshop.set.model.interfaces.Pattern;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.lang.judgements.HasType;
import com.workshop.set.model.lang.judgements.HasValue;

/**
 * The Gensym class implements a symbol generator for generic subclasses S
 * of Symbol. It generates fresh names and unique names that can be compared for
 * equality, on each call to its "generate" method. the Gensym itself can also be
 * compared with equality - two Gensyms are considered equal if they have generated the
 * same number of symbols, thereby being in the same internal state. Symbols from different
 * symbol generators are never considered equal.
 *
 * Each instance of Gensym is considered unique. Therefore, it relies on the default
 * equality comparison, which is true only in the case of reflexivity.
 *
 */
public class TNameGenerator
    implements Gensym {
    public class TName implements Symbol, Pattern {
        private TName( Gensym factory, long index, String hint ) {
            this.readable = hint;
            this.factory = factory;
            this.index = index;
        }

        public final String readable;
        private final Gensym factory;
        private final long index;


        public boolean matches( long index ) { return index == this.index; }
        public boolean matches( Gensym factory ) { return factory.equals(this.factory); }

        public TName create( Gensym factory, long index, String h ) {
            return new TName( factory, index, h );
        }

        /**
         * Two symbols are equal if and only if they were produced by the same
         * symbol factory, and they are indexed by the same de Bruijn number.
         *
         * @param o an object to check this Symbol against
         * @return a boolean indicating whether this and o are equal.
         */
        @Override
        public boolean equals( Object o ) {
            try {
                return ((TName)o).matches( index )
                    && ((TName)o).matches( factory );
            } catch ( ClassCastException _ ) {
                return false;
            }
        }
        @Override
        public String toString() {
            return ( readable.isEmpty() ) ? "?" + Long.toString( index ) : readable + Long.toString( index );
        }

        @Override
        public Environment<Term> type( Environment<Term> gamma )
            throws ProofFailureException, TypecheckingException {
                try {
                    Term t =  gamma.proves( this );
                           gamma.compute( this, t );
                    return gamma;
                } catch ( ProofFailureException e ) {
                    throw new TypecheckingException( this, gamma, "Unbound Identifier" );
                }
        }

        @Override
        public Term reduce() {
            return this;
        }

        @Override
        public Term substitute( Term x, Symbol y ) {
            if ( y.equals( this ) ) return x;
            else return this;
        }

        @Override
        public Set<Symbol> free() {
            return new HashSet<Symbol>( Arrays.asList( this ) );
        }

        @Override
        public Set<HasValue> bind( Term value ) {
            return new HashSet<HasValue>(Arrays.asList( new HasValue( this, value ) ) );
        }

        @Override
        public boolean binds( Symbol n ) {
            return n.equals( this );
        }

        @Override
        public Set<Judgement<Term>> decompose( Environment<Term> gamma )
            throws ProofFailureException, TypecheckingException {
            Term ty = gamma.proves( this );
            if ( ty == null ) throw new TypecheckingException( this, gamma, "Decomposition Error" );
            return new HashSet<Judgement<Term>>( Arrays.asList( new HasType( this, ty ) ) );
        }

        @Override
        public String name() {
            return readable;
        }

        @Override
        public int hashCode() {

            int a   = (int)index;
            int b   = (( readable.isEmpty() ) ? "" : readable).hashCode();

            return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31)));

        }

        @Override
        public Set<Symbol> names() {
            return new LinkedHashSet<Symbol>( Arrays.asList(this) );
        }
    }

    public TNameGenerator() { state = 0L; }

    private long state;

    /**
     * This method generates a fresh, unique symbol, with respect to this
     * Symbol Generator's internal state. Symbols can be compared for equality
     * in constant time.
     *
     * @return a new Symbol as generated by this symbol factory.
     */
    public TName generate() { return new TName( this, state++, "" ); }
    public TName generate( String h ) { return new TName( this, state++, h ); }


}