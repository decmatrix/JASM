package com.sokolovskyi.jasm.compiler.syntax;

public final class SyntaxErrors {
    private SyntaxErrors(){}

    static final String err = " SE <0x0> Syntactic error";
    static final String Ox1 = " SE <0x1> Expected: instruction or directive";
    static final String Ox2 = " SE <0x2> Symbol not defined: ";
    static final String Ox3 = " SE <0x3> Operator expected";
    static final String Ox4 = " SE <0x4> Extra characters on line";
    static final String Ox5 = " SE <0x5> Missing data";
    static final String Ox6 = " SE <0x6> Immediate mode illegal";
    static final String Ox7 = " SE <0x7> Expected: comma";
    static final String Ox8 = " SE <0x8> Operand types must match";
    static final String Ox9 = " SE <0x9> Must be associated with code";
    static final String OxA = " SE <0xA> Open parenthesis or bracket";
    static final String OxB = " SE <0xB> Illegal use of register";
    static final String OxC = " SE <0xC> Left operand must have segment";
    static final String OxD = " SE <0xD> Operand must have size";
    static final String OxE = " SE <0xE> Directive must be used correctly";
    static final String OxF = " SE <0xF> Directive SEGMENT must be used correctly with labels: DATA or CODE";
    static final String Ox10 = " SE <0x10> Symbol is multidefined: ";
    static final String Ox11 = " SE <0x11> Improper operand type";
    static final String Ox12 = " SE <0x12> Operand expected";
}
