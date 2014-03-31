package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.PatternMatchException;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasType;
import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

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
            return ((TTuple)o).domain.equals( domain )
                && ((TTuple)o).range.equals( range );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "( " + domain.toString() + ", " + range.toString() + " )";
    }

    @Override
    public Term type( Context gamma )
        throws TypecheckingException {
        try {
            Term a = domain.type( gamma ),
                 b = range.type( gamma );

            if ( a!=null && b!=null ) {

               // TODO implement naming contexts
               TNameGenerator g = new TNameGenerator();
               return new TSum( g.generate("_"), a, b );

            } else throw new TypecheckingException( this, gamma );
        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma );
        }
    }

    @Override
    public Term step( Environment eta ) {
        // TODO : define small step evaluation
        return null;
    }

    @Override
    public Value evaluate( Environment eta ) {
        // TODO : define big step evaluation
        return null;
    }

    @Override
    public Term substitute( Term x, TNameGenerator.TName y ) {
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
    public boolean binds( TNameGenerator.TName n ) {
        boolean b = false;
        try {
            b = ((Pattern)domain).binds( n );
            b |= ((Pattern)range).binds( n );
        } catch ( ClassCastException _ ) {}
        finally { return b; }

    }

    @Override
    public Set<Judgement> decompose( Context gamma )
        throws TypecheckingException {
        try {
            Set<Judgement> j = new HashSet<Judgement>();

            Term ty = gamma.proves( this ); // retrieve the type of this
            Term domTy = ((TSum)ty).type;
            Term codTy = ((TSum)ty).body;

            try { j.addAll( ((Pattern)domain).decompose( gamma.extend( new HasType(domain, domTy) ) ) ); }
            catch ( ClassCastException _ ) {}

            try { j.addAll( ((Pattern)range).decompose( gamma.extend( new HasType(range, codTy) ) ) ); }
            catch ( ClassCastException _ ) {}

            return j;

        } catch ( ClassCastException _ ) {
            throw new TypecheckingException( this, gamma, "Decomposition Error" );
        }
    }
}
