package com.workshop.set.model.run;

import com.workshop.set.model.interfaces.Gensym;
import com.workshop.set.model.interfaces.Term;
import com.workshop.set.model.interfaces.Workflow;
import com.workshop.set.model.lang.exceptions.LexException;
import com.workshop.set.model.lang.exceptions.ParseException;
import com.workshop.set.model.lang.parser.EXPRParser;
import com.workshop.set.model.lang.parser.Lexer;

import java.io.IOException;

/**
 * A Parse Workflow on Strings implements parsing from Strings into term
 */
public class ParseWorkflow implements Workflow<String,Term> {
    public ParseWorkflow( Gensym g ) {
        lexer = new Lexer(); parser = new EXPRParser( g );
    }

    private Lexer lexer;
    private EXPRParser parser;

    @Override
    public Term run( String arg ) throws ParseException, LexException, IOException {
        return parser.parse( lexer.lex( arg ) );
    }
}
