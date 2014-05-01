package com.workshop.set.model.lang.parser;

import com.workshop.set.model.lang.parser.Grammar.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by nicschumann on 4/22/14.
 */
public class Item {
    public Item( Production rule, int position, List<TERMINAL> lookahead ) {
        this.rule = rule; this.dotPosition = position; this.lookahead = lookahead;
    }

    private Production rule;
    private int dotPosition;
    private List<TERMINAL> lookahead;

    /**
     * this method returns the current position of the dot in this item.
     * @return an integer representing the dot position
     */
    public int getPosition() { return dotPosition; }

    /**
     * this method advances the dot by one position in this production.
     */
    public void shift() { dotPosition += 1; }

    /**
     * this method returns the look-ahead for this particular entry in the item-set
     * @return a set of tokens representing the look-ahead on this item
     */
    public List<TERMINAL> lookahead() { return new ArrayList<>( lookahead ); }

    /**
     * this method returns the left hand side of the production rule that this
     * item represents
     * @return a NONTERMINAL representing the symbol that this non-terminal proves
     */
    public NONTERMINAL lefthandside() { return rule.lhs(); }

    /**
     * this method returns the right hand side of the production rule, which is a
     * set of terminal and non-terminal symbols.
     * @return a list of ITEMs representing this items full right hand side
     */
    public List<ITEM> righthandside() { return rule.rhs(); }

    /**
     * this method returns the set of terminal or non-terminal symbols that
     * occur after the dot in the production rule.
     * @return a list of ITEMs that occur after the dot in this production rule.
     */
    public List<ITEM> rest() {
        ArrayList<ITEM> rest = new ArrayList<>();
        List<ITEM> full = righthandside();
        for ( int i = 0; i < full.size(); i ++ ) {
            if ( i < dotPosition ) continue;
            rest.add( full.get( i ) );
        }
        return rest;
    }













    @Override
    public boolean equals( Object o ) {
        try {
            Item item = ((Item) o);
            return item.lefthandside().equals( rule.lhs() )
                && item.righthandside().equals( rule.rhs() )
                && item.getPosition() == dotPosition
                && item.lookahead().equals( lookahead );
        } catch ( ClassCastException _ ) {
            return false;
        }
    }


}
