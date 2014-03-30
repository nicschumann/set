package com.workshop.set.lang.core;

import com.workshop.set.interfaces.*;
import com.workshop.set.lang.exceptions.TypecheckingException;
import com.workshop.set.lang.judgements.HasValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicschumann on 3/29/14.
 */
public class TTuple implements Pattern {
    public TTuple( Pattern domain, Pattern range ) {
        this.domain = domain;
        this.range = range;
    }

    public final Pattern domain;
    public final Pattern range;

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
                // TODO ascribe a type, and return it.
                return null;
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
        return null;
    }

    @Override
    public Pattern substitute( Term x, TNameGenerator.TName y ) {
        return new TTuple( domain.substitute(x,y), range.substitute(x,y) );
    }


    @Override
    public Set<HasValue> bind( Term value ) {
        return null;
    }
    @Override
    public boolean binds( TNameGenerator.TName n ) {
        return domain.binds( n ) || range.binds( n );
    }
    @Override
    public Set<TNameGenerator.TName> names( ) {
        HashSet<TNameGenerator.TName> s = new HashSet<TNameGenerator.TName>();
        s.addAll( domain.names() );
        s.addAll( range.names() );
        return s;
    }
}
