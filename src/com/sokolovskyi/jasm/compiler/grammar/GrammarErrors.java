package com.sokolovskyi.jasm.compiler.grammar;

public final class GrammarErrors {
    private GrammarErrors(){}

    static final String Ox1 = " GE <0x1> Data emitted with no segment";
    static final String Ox2 = " GE <0x2> There are too many CODE or DATA segments";
    static final String Ox3 = " GE <0x3> There are too many END";
    static final String Ox4 = " GE <0x4> End of file, no END directive";
}
