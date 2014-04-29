package com.workshop.set.model.lang.parser;

import java.util.Set;

/**
 * Created by nicschumann on 4/22/14.
 */
public class ParserIII {


















    public Set<Item> merge( Set<Item> a, Set<Item> b ) {
        a.addAll( b ); return a;
    }

    public boolean predictEquality( Set<Item> a, Set<Item> b ) {
        return a.equals( b );
    }



}
