package com.sokolovskyi.jasm.compiler.lexemes;



 public final class Lexemes {

    private Lexemes(){}

    //mnemonic constants
    public final static String REGISTER_8 = "Reg 8";
    public final static String REGISTER_32 = "Reg 32";
    public final static  String REGISTER_S = "Reg stack";
    public final static String DIRECTIVE = "Directive";
    public final static String DATATYPE = "Data type";
    public final static String ASMCOMMAND = "Machine command";
    public final static String LITERAL = "Char literal";

    //other constants
    public final static String UNKNOWN_LEXEME = "UNKNOWN LEXEME";
    public final static String IDENTIFIER = "Identifier";
    public final static String DEC_CONSTANT ="Dec constant";
    public final static String HEX_CONSTANT ="Hex constant";
    public final static String BIN_CONSTANT ="Bin constant";


    //registers 8 bits
    public final static String[] REGISTERS_8 = {"AL", "AH", "BL", "BH", "CL", "CH",
    "DL", "DH", "BPL", "SIL", "DIL", "SPL", "IP"};
    //registers 32 bits
    public final static String[] REGISTERS_32 = {"EAX", "EBX", "ECX", "EDX", "EBP",
    "ESI", "EDI", "ESP", "EIP"};
    //registers of stack
    public final static String[] REGISTERS_S = {"CS", "FS", "DS", "ES", "GS", "SS"};
    //directives
    public final static String[] DIRECTIVES = {"SEGMENT", "ENDS", "END", "DWORD", "WORD", "PTR", "BYTE"};
    //data types
    public final static String[] DATATYPES = {"DB", "DW", "DD"};
    //assembly commands
    public final static String[] ASM_COMMANDS = {"CLI", "INC", "DEC", "ADD", "OR", "AND",
    "MOV", "CMP", "JB", "JMP"};
    //literals
    public final static String[] LITERALS = {",", "+", "-", "*", "[", "]", "(", ")", ":"};

    //all mnemonics
    public final static String[] ALL_MNEMONICS = {"AL", "AH", "BL", "BH", "CL", "CH",
            "DL", "DH", "BPL", "SIL", "DIL", "SPL", "IP", "EAX", "EBX", "ECX", "EDX", "EBP",
            "ESI", "EDI", "ESP", "EIP", "CS", "FS", "DS", "ES", "GS", "SS", "SEGMENT", "ENDS",
            "END", "DWORD", "WORD", "PTR", "BYTE", "DB", "DW", "DD", "CLI", "INC", "DEC", "ADD", "OR", "AND",
            "MOV", "CMP", "JB", "JMP", ",", "+", "-", "*", "[", "]", "(", ")", ":"};
}
