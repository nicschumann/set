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
    public static class Terminal {
        public static abstract class TERM {
            public TERM( int position ) { streamposition = position; }
            protected int streamposition;
        }
        public static class LAMBDA extends TERM {
            public LAMBDA( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LAMBDA; }
            public String toString() { return "LAMBDA"; }
        }
        public static class FORALL extends TERM {
            public FORALL( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof FORALL; }
            public String toString() { return "FORALL"; }
        }
        public static class SUM extends TERM {
            public SUM( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof SUM; }
            public String toString() { return "SUM"; }
        }
        public static class COLON extends TERM {
            public COLON( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof COLON; }
            public String toString() { return "COLON"; }
        }
        public static class PERIOD extends TERM {
            public PERIOD( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PERIOD; }
            public String toString() { return "PERIOD"; }
        }
        public static class LPAREN extends TERM {
            public LPAREN( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LPAREN; }
            public String toString() { return "LPAREN"; }
        }
        public static class RPAREN extends TERM {
            public RPAREN( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof RPAREN; }
            public String toString() { return "RPAREN"; }
        }
        public static class LBRACKET extends TERM {
            public LBRACKET( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LBRACKET; }
            public String toString() { return "LBRACKET"; }
        }
        public static class RBRACKET extends TERM {
            public RBRACKET( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof RBRACKET; }
            public String toString() { return "RBRACKET"; }
        }
        public static class LBRACE extends TERM {
            public LBRACE( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof LBRACE; }
            public String toString() { return "LBRACE"; }
        }
        public static class RBRACE extends TERM {
            public RBRACE( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof RBRACE; }
            public String toString() { return "RBRACE"; }
        }
        public static class EQUALS extends TERM {
            public EQUALS( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof EQUALS; }
            public String toString() { return "EQUALS"; }
        }
        public static class PLUS extends TERM {
            public PLUS( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof PLUS; }
            public String toString() { return "PLUS"; }
        }
        public static class MINUS extends TERM {
            public MINUS( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof MINUS; }
            public String toString() { return "MINUS"; }
        }
        public static class TIMES extends TERM {
            public TIMES( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof TIMES; }
            public String toString() { return "TIMES"; }
        }
        public static class DIV extends TERM {
            public DIV( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof DIV; }
            public String toString() { return "DIV"; }
        }
        public static class UNIV extends TERM {
            public UNIV( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof UNIV; }
            public String toString() { return "UNIV"; }
        }
        public static class FIELD extends TERM {
            public FIELD( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof FIELD; }
            public String toString() { return "FIELD"; }
        }
        public static class CARROT extends TERM {
            public CARROT( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof CARROT; }
            public String toString() { return "CARROT"; }
        }
        public static class COMMA extends TERM {
            public COMMA( int position ) { super(position); }
            public boolean equals( Object o ) { return o instanceof COMMA; }
            public String toString() { return "COMMA"; }
        }
        public static class ID extends TERM {
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
        public static class NUM extends TERM {
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

    }
    public enum Nonterminal {
        TERM, PATTERN, JUDGEMENT, OPERATOR
    }
}
