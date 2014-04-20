package com.workshop.set.model.lang.core;

import java.util.HashSet;
import java.util.Set;

import com.workshop.set.model.interfaces.Environment;
import com.workshop.set.model.interfaces.Judgement;
import com.workshop.set.model.interfaces.Pattern;
import com.workshop.set.model.interfaces.Symbol;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.lang.engines.Decide;
import com.workshop.set.model.lang.exceptions.EvaluationException;
import com.workshop.set.model.lang.exceptions.PatternMatchException;
import com.workshop.set.model.lang.exceptions.ProofFailureException;
import com.workshop.set.model.lang.exceptions.TypecheckingException;
import com.workshop.set.model.lang.judgements.HasValue;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TTuple implements Pattern {
    public TTuple( Term domain, Term range ) {
        this.domain = domain;
        this.range = range;
    }

    public final Term domain;
    public final Term range;

    @Override
    public boolean equals( Object o ) {
        try {
            return Decide.alpha_equivalence(this, (TTuple) o, new HashSet<Symbol>(), new HashSet<Symbol>());
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "( " + domain.toString() + ", " + range.toString() + " )";
    }

    @Override
    public Environment<Term> type( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {

            Term a = (domain.type( gamma )).proves( domain ),
                 b = (range.type( gamma )).proves( range );

            if ( a!=null && b!=null ) {
               TSum t = new TSum( gamma.freshname("_"), a, b );

                      gamma.compute( this, t );
               return gamma.extend( this, t );

            } else throw new TypecheckingException( this, gamma );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    @Override
    public Term reduce() throws EvaluationException {
        return new TTuple( domain.reduce(), range.reduce() );
    }

    @Override
    public Term substitute( Term x, Symbol y ) {
        return new TTuple( domain.substitute(x,y), range.substitute(x,y) );
    }


    @Override
    public Set<HasValue> bind( Term value )
        throws PatternMatchException {
        try {
            TTuple t = (TTuple)value;
            HashSet<HasValue> s = new HashSet<HasValue>();

            s.addAll(domain.bind(t.domain));
            s.addAll(range.bind(t.range));
            return s;

        } catch ( ClassCastException _ ) {
            throw new PatternMatchException( this, value );
        }
    }
    @Override
    public boolean binds( Symbol n ) {
        boolean b = false;
        try {
            b = ((Pattern)domain).binds( n );
            b |= ((Pattern)range).binds( n );
        } catch ( ClassCastException _ ) {}
//        finally { return b; }
        return b;

    }

    @Override
    public Set<Judgement<Term>> decompose( Environment<Term> gamma )
        throws ProofFailureException, TypecheckingException {
        try {
            Set<Judgement<Term>> j = new HashSet<Judgement<Term>>();

            Term ty = gamma.proves( this ); // retrieve the type of this
            Term domTy = ((TSum)ty).type;
            Term codTy = ((TSum)ty).body;

            try { j.addAll( ((Pattern)domain).decompose( gamma.extend( domain, domTy ) ) ); }
            catch ( ClassCastException _ ) {}

            try { j.addAll( ((Pattern)range).decompose( gamma.extend( range, codTy ) ) ); }
            catch ( ClassCastException _ ) {}

            return j;

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma, "Decomposition Error" );
        }
    }

    @Override
    public Set<Symbol> free() {
        return names();
    }

    @Override
    public int hashCode() {

        int a   = domain.hashCode();
        int b   = range.hashCode();

        return 37 * (37 * ( (a ^ (a >>> 31))) + (b ^ (b >>> 31))) + 1;

    }

    @Override
    public Set<Symbol> names() {
        Set<Symbol> n = new HashSet<Symbol>();
        try {
            n.addAll( ((Pattern)domain).names() );
        } catch ( ClassCastException _ ) {}
        try {
            n.addAll( ((Pattern)range).names() );
        } catch ( ClassCastException _ ) {}
        return n;
    }
}
