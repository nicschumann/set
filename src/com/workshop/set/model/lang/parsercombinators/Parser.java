package com.workshop.set.model.lang.parsercombinators;

import java.util.List;

/**
 * Core of a Lightweight ParserCombinator Library
 */
public interface Parser {
    public ParseResult parse( List<ConcreteSyntax> tkns );
}
