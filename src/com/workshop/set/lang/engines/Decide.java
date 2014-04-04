package com.workshop.set.lang.engines;

import com.workshop.set.interfaces.Symbol;
import com.workshop.set.interfaces.Term;
import com.workshop.set.lang.core.*;

import java.util.Set;

/**
 * This class uses adhoc polymorphism to define alpha equivalence on terms. Basically,
 * It's not sustainable. Figure out a better way to do this.
 */
public class Decide {
    public static boolean alpha_equivalence( Term t1, Term t2, Set<Symbol> g1, Set<Symbol> g2 ) {

        System.out.println( "AD - HOC" );

        try {
            return alpha_equivalence( (TAbstraction)t1, (TAbstraction)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TAll)t1, (TAll)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TSum)t1, (TSum)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}

        try {
            return alpha_equivalence( (TApplication)t1, (TApplication)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}

        try {
            return alpha_equivalence( (TExponential)t1, (TExponential)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TCollection)t1, (TCollection)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TUniverse)t1, (TUniverse)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TField)t1, (TField)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TJudgement)t1, (TJudgement)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TMultiplicative)t1, (TMultiplicative)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TAdditive)t1, (TAdditive)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TNameGenerator.TName)t1, (TNameGenerator.TName)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TScalar)t1, (TScalar)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TSet)t1, (TSet)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TVector)t1, (TVector)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}
        try {
            return alpha_equivalence( (TTuple)t1, (TTuple)t2, g1, g2 );
        } catch ( ClassCastException _ ) {}



        return false;
    }

    public static boolean alpha_equivalence( TAbstraction t1, TAbstraction t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        System.out.println( "Abstraction" );
        g1.addAll( t1.binder.names() );
        g2.addAll( t2.binder.names() );
        return alpha_equivalence( t1.type, t2.type, g1, g2 )
            && alpha_equivalence( t1.body, t2.body, g1, g2 );
    }
    public static boolean alpha_equivalence( TAll t1, TAll t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        g1.addAll( t1.binder.names() );
        g2.addAll( t2.binder.names() );
        return alpha_equivalence( t1.type, t2.type, g1, g2 )
                && alpha_equivalence( t1.body, t2.body, g1, g2 );
    }
    public static boolean alpha_equivalence( TSum t1, TSum t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        g1.addAll( t1.binder.names() );
        g2.addAll( t2.binder.names() );
        return alpha_equivalence( t1.type, t2.type, g1, g2 )
                && alpha_equivalence( t1.body, t2.body, g1, g2 );
    }
    public static boolean alpha_equivalence( TApplication t1, TApplication t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return alpha_equivalence( t1.implication, t2.implication, g1, g2 )
            && alpha_equivalence( t1.argument, t2.argument, g1, g2 )
            && t1.equals( t2 );
    }
    public static boolean alpha_equivalence( TNameGenerator.TName t1, TNameGenerator.TName t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return g1.contains( t1 ) && g2.contains( t2 ) || t1.equals( t2 );
    }
    public static boolean alpha_equivalence( TAdditive t1, TAdditive t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return alpha_equivalence( t1.addand, t2.addand, g1, g2 );
    }
    public static boolean alpha_equivalence( TMultiplicative t1, TMultiplicative t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return alpha_equivalence( t1.multiplicand, t2.multiplicand, g1, g2 );
    }
    public static boolean alpha_equivalence( TJudgement t1, TJudgement t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return alpha_equivalence( t1.left, t2.left, g1, g2 )
            && alpha_equivalence( t1.right, t2.right, g1, g2 );
    }

    public static boolean alpha_equivalence( TTuple t1, TTuple t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return alpha_equivalence( t1.domain, t2.domain, g1, g2 )
            && alpha_equivalence( t1.range, t2.range, g1, g2 );
    }
    public static boolean alpha_equivalence( TScalar t1, TScalar t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return t1.index == t2.index;
    }

    public static boolean alpha_equivalence( TCollection t1, TCollection t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return alpha_equivalence( t1.contents, t2.contents, g1, g2);
    }
    public static boolean alpha_equivalence( TExponential t1, TExponential t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return t1.exp == t2.exp && alpha_equivalence( t1.base, t2.base, g1, g2 );

    }

    public static boolean alpha_equivalence( TField t1, TField t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return true;
    }
    public static boolean alpha_equivalence( TUniverse t1, TUniverse t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        return t1.level == t2.level;
    }


    // a-equivalence on collections
    public static boolean alpha_equivalence( TSet t1, TSet t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        boolean fold = true;
        if ( t1.elements().size() != t2.elements().size() ) return !fold;
        else {
            for ( int i = 0; i < t1.elements().size(); i ++ ) {
                fold &= alpha_equivalence( t1.elements().get( i ), t2.elements().get( i ), g1, g2 );
            }
            return fold;
        }
    }
    public static boolean alpha_equivalence( TVector t1, TVector t2, Set<Symbol> g1, Set<Symbol> g2 ) {
        boolean fold = true;
        if ( t1.components().size() != t2.components().size() ) return !fold;
        else {
            for ( int i = 0; i < t1.components().size(); i ++ ) {
                fold &= alpha_equivalence( t1.components().get( i ), t2.components().get( i ), g1, g2 );
            }
            return fold;
        }
    }


}
