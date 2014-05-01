package com.workshop.set.model.lang.parser;

import java.util.List;

import com.workshop.set.model.lang.parser.Grammar.*;

/**
 * This class models a grammar rule of the form:
 *  NONTERMINAL <- {NONTERMINAL,TERMINAL}*. It encodes a method for producing the named
 *  NONTERMINAL symbol from the list of Grammar ITEMS (ie, terminals and non-terminals ) supplied.
 */
public abstract class Production {
    public Production( NONTERMINAL lhs, List<ITEM> rhs ) {
        proving = lhs;
        proof = rhs;
    }

    private NONTERMINAL proving;
    private List<ITEM> proof;

    public NONTERMINAL lhs() { return proving; }
    public List<ITEM> rhs() { return proof; }
}
