package com.sokolovskyi.jasm.compiler.lexemes;



 final class Lexemes {

    private Lexemes(){}

    //mnemonic constants
    final static String REGISTER_8 = "Reg 8";
    final static String REGISTER_32 = "Reg 32";
    final static  String REGISTER_S = "Reg stack";
    final static String DIRECTIVE = "Directive";
    final static String DATATYPE = "Data type";
    final static String ASMCOMMAND = "Machine command";
    final static String LITERAL = "Char literal";

    //other constants
    final static String UNKNOWN_LEXEME = "UNKNOWN LEXEME";
    final static String IDENTIFIER = "Identifier";
    final static String DEC_CONSTANT ="Dec constant";
    final static String HEX_CONSTANT ="Hex constant";
    final static String BIN_CONSTANT ="Bin constant";


    //registers 8 bits
    final static String[] REGISTERS_8 = {"AL", "AH", "BL", "BH", "CL", "CH",
    "DL", "DH", "BPL", "SIL", "DIL", "SPL", "IP"};
    //registers 32 bits
    final static String[] REGISTERS_32 = {"EAX", "EBX", "ECX", "EDX", "EBP",
    "ESI", "EDI", "ESP", "EIP"};
    //registers of stack
    final static String[] REGISTERS_S = {"CS", "FS", "DS", "ES", "GS", "SS"};
    //directives
    final static String[] DIRECTIVES = {"SEGMENT", "ENDS", "END", "DWORD", "WORD", "PTR", "BYTE"};
    //data types
    final static String[] DATATYPES = {"DB", "DW", "DD"};
    //assembly commands
    final static String[] ASM_COMMANDS = {"CLI", "INC", "DEC", "ADD", "OR", "AND",
    "MOV", "CMP", "JB", "JMP"};
    //literals
    final static String[] LITERALS = {",", "+", "-", "*", "[", "]", "(", ")", ":"};

    //all mnemonics
    final static String[] ALL_MNEMONICS = {"AL", "AH", "BL", "BH", "CL", "CH",
            "DL", "DH", "BPL", "SIL", "DIL", "SPL", "IP", "EAX", "EBX", "ECX", "EDX", "EBP",
            "ESI", "EDI", "ESP", "EIP", "CS", "FS", "DS", "ES", "GS", "SS", "SEGMENT", "ENDS",
            "END", "DWORD", "WORD", "PTR", "BYTE", "DB", "DW", "DD", "CLI", "INC", "DEC", "ADD", "OR", "AND",
            "MOV", "CMP", "JB", "JMP", ",", "+", "-", "*", "[", "]", "(", ")", ":"};
}
