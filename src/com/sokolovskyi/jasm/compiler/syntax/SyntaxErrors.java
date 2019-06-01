package com.sokolovskyi.jasm.compiler.syntax;

public final class SyntaxErrors {
    private SyntaxErrors(){}

    static final String err = " SE <err> Syntactic error";
    static final String Ox1 = " SE <0x1> Expected: instruction or directive";
    static final String Ox2 = " SE <0x2> Symbol not defined: ";
    static final String Ox3 = " SE <0x3> Operand expected ";
    static final String Ox4 = " SE <0x4> Extra characters on line";
    static final String Ox5 = " SE <0x5> Missing data; zero assumed";
    static final String Ox6 = " SE <0x6> Immediate mode illegal";
    static final String Ox7 = " SE <0x7> Expected: comma";
}
