package com.workshop.set.model.lang.parser;

/**
 * This class encodes the terminals and non-terminals of the intermediate constraint language
 * for Set. for reference:
 *
 *
 * T1, T2      :=      lambda V : T1 . T2              ( TAbstraction )
 *             |       forall V : T1 . T2              ( TAll )
 *             |       sum V : T1 . T2                 ( TSum )
 *             |       T1 J T2                         ( TJudgement )
 *             |       T1 T2                           ( TApplication )
 *             |       Univ N                          ( TUniverse )
 *             |       Field                           ( TField )
 *             |       (V)                             ( TPattern )
 *
 * V1, V2      :=      ( V1 V2 ... VN )                ( TVector )
 *             |       { V1, V2, ..., VN }             ( TSet )
 *             |       ( V1, V2 )                      ( TTuple )
 *             |       V1 O V2                         ( TOp )
 *             |       s                               ( TScalar )
 *             |       a                               ( TName )
 *
 * J           :=      =
 *
 * O           :=      *
 *             |       -
 *             |       +
 *             |       /
 *
 * Terminals:
 *          LAMBDA,
 *          FORALL,
 *          SUM,
 *          COLON,
 *          PERIOD,
 *          COMMA,
 *          LPAREN,
 *          LBRACKET,
 *          LBRACE,
 *          RPAREN,
 *          RBRACKET,
 *          RBRACE,
 *          EQUALS,
 *          PLUS,
 *          MINUS,
 *          TIMES,
 *          SLASH,
 *          SCALAR(number),
 *          ID(string)
 *
 * Nonterminals:
 *          JUDGEMENT,
 *          OPERATOR,
 *          PATTERN,
 *          TERM
 *
 *
 *
 * @author nic
 *
 */

public class Grammar {

        public static abstract class ITEM {
            public ITEM( int position ) { streamposition = position; }
            protected int streamposition;
        }
        public static abstract class TERMINAL extends ITEM {
            public TERMINAL( int position ) { super( position ); }
        }

        public static class LAMBDA extends TERMINAL {
            public LAMBDA( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LAMBDA; }
            public String toString() { return "LAMBDA"; }
        }
        public static class FORALL extends TERMINAL {
            public FORALL( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof FORALL; }
            public String toString() { return "FORALL"; }
        }
        public static class SUM extends TERMINAL {
            public SUM( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof SUM; }
            public String toString() { return "SUM"; }
        }
        public static class COLON extends TERMINAL {
            public COLON( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof COLON; }
            public String toString() { return "COLON"; }
        }
        public static class PERIOD extends TERMINAL {
            public PERIOD( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PERIOD; }
            public String toString() { return "PERIOD"; }
        }
        public static class LPAREN extends TERMINAL {
            public LPAREN( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LPAREN; }
            public String toString() { return "LPAREN"; }
        }
        public static class RPAREN extends TERMINAL {
            public RPAREN( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof RPAREN; }
            public String toString() { return "RPAREN"; }
        }
        public static class LBRACKET extends TERMINAL {
            public LBRACKET( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LBRACKET; }
            public String toString() { return "LBRACKET"; }
        }
        public static class RBRACKET extends TERMINAL {
            public RBRACKET( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof RBRACKET; }
            public String toString() { return "RBRACKET"; }
        }
        public static class LBRACE extends TERMINAL {
            public LBRACE( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LBRACE; }
            public String toString() { return "LBRACE"; }
        }
        public static class RBRACE extends TERMINAL {
            public RBRACE( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof RBRACE; }
            public String toString() { return "RBRACE"; }
        }
        public static class EQUALS extends TERMINAL {
            public EQUALS( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof EQUALS; }
            public String toString() { return "EQUALS"; }
        }
        public static class PLUS extends TERMINAL {
            public PLUS( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PLUS; }
            public String toString() { return "PLUS"; }
        }
        public static class MINUS extends TERMINAL {
            public MINUS( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof MINUS; }
            public String toString() { return "MINUS"; }
        }
        public static class TIMES extends TERMINAL {
            public TIMES( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof TIMES; }
            public String toString() { return "TIMES"; }
        }
        public static class DIV extends TERMINAL {
            public DIV( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof DIV; }
            public String toString() { return "DIV"; }
        }
        public static class UNIV extends TERMINAL {
            public UNIV( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof UNIV; }
            public String toString() { return "UNIV"; }
        }
        public static class FIELD extends TERMINAL {
            public FIELD( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof FIELD; }
            public String toString() { return "FIELD"; }
        }
        public static class CARROT extends TERMINAL {
            public CARROT( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof CARROT; }
            public String toString() { return "CARROT"; }
        }
        public static class COMMA extends TERMINAL {
            public COMMA( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof COMMA; }
            public String toString() { return "COMMA"; }
        }
        public static class PAR extends TERMINAL {
            public PAR( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PAR; }
            public String toString() { return "PAR"; }
        }
        public static class PERP extends TERMINAL {
            public PERP( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PERP; }
            public String toString() { return "PERP"; }
        }
        public static class END extends TERMINAL {
            public END( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof END; }
            public String toString() { return "$"; }
        }
        public static class ID extends TERMINAL {
            public ID( String value, int position ) { super(position); this.value = value; }
            public final String value;
            public boolean equals( Object o ) {
                try {
                    return value.equals( ((ID)o).value );
                } catch ( ClassCastException _ ) {
                    return false;
                }
            }
            public String toString() { return "ID:\""+value+"\""; }
        }
        public static class NUM extends TERMINAL {
            public NUM( double value, int position ) { super(position); this.value = value; }
            public final double value;
            public boolean equals( Object o ) {
                try {
                    return value == ((NUM)o).value;
                } catch ( ClassCastException _ ) {
                    return false;
                }
            }
            public String toString() { return "NUM:\""+value+"\""; }
        }

        public static abstract class NONTERMINAL extends ITEM {
            public NONTERMINAL( int position ) { super(position); }
            public boolean equals( Object o ) {
                return o instanceof TERM || o instanceof  PATTERN
                    || o instanceof JUDGEMENT || o instanceof OPERATOR;
            }
        }
        public static class TERM extends NONTERMINAL {
            public TERM( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof TERM; }
            public String toString() { return "TERM"; }
        }
        public static class PATTERN extends NONTERMINAL {
            public PATTERN( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PATTERN; }
            public String toString() { return "PATTERN"; }
        }
        public static class JUDGEMENT extends NONTERMINAL {
            public JUDGEMENT( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof JUDGEMENT; }
            public String toString() { return "JUDGEMENT"; }
        }
        public static class OPERATOR extends NONTERMINAL {
            public OPERATOR( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof OPERATOR; }
            public String toString() { return "OPERATOR"; }
        }




}
