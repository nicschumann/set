package com.workshop.set.lang.environments;

import com.workshop.set.interfaces.Context;
import com.workshop.set.interfaces.Judgement;
import com.workshop.set.interfaces.Term;

import java.util.Map;
import java.util.Set;

/**
 * Created by nicschumann on 3/30/14.
 */
public class EmptyTyping implements Context {

    @Override
    public boolean contains( Judgement e ) { return false; }

    @Override
    public Context extend( Judgement e ) { return new Typing().extend( e ); }

    @Override
    public Context extend( Set<Judgement> es ) {
        return new Typing().extend( es );
    }

    @Override
    public Term proves( Term term ) { return null; }

    @Override
    public boolean wellformed() { return true;}

    @Override
    public String toString() {
        return "Empty Context";
    }
}
