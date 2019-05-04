package com.sokolovskyi.jasm.compiler.listing;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.util.HashMap;
import java.util.Map;

final class Opcode {
    private Opcode(){}

    private static Map<String, String> regs32;
    private static Map<String, String> regs8;

    private static Map<String, String> regs8mrm;
    private static Map<String, String> regs32mrm;

    //FIXME it is a no-go, rebuild and don't use static {} !!!
    static{
        regs32 = new HashMap<>();
        regs8 = new HashMap<>();

        regs8mrm = new HashMap<>();
        regs32mrm = new HashMap<>();

        regs32.put(Lexemes.REGISTERS_32[0], "000");
        regs32.put(Lexemes.REGISTERS_32[1], "011");
        regs32.put(Lexemes.REGISTERS_32[2], "001");
        regs32.put(Lexemes.REGISTERS_32[3], "010");
        regs32.put(Lexemes.REGISTERS_32[4], "101");
        regs32.put(Lexemes.REGISTERS_32[5], "110");
        regs32.put(Lexemes.REGISTERS_32[6], "111");
        regs32.put(Lexemes.REGISTERS_32[7], "100");

        regs8.put(Lexemes.REGISTERS_8[0], "000");
        regs8.put(Lexemes.REGISTERS_8[1], "100");
        regs8.put(Lexemes.REGISTERS_8[2], "011");
        regs8.put(Lexemes.REGISTERS_8[3], "111");
        regs8.put(Lexemes.REGISTERS_8[4], "001");
        regs8.put(Lexemes.REGISTERS_8[5], "101");
        regs8.put(Lexemes.REGISTERS_8[6], "010");
        regs8.put(Lexemes.REGISTERS_8[7], "110");

        //TODO do better gen mod + reg + r/m
        regs8mrm.put(Lexemes.REGISTERS_8[0], "44");
        regs8mrm.put(Lexemes.REGISTERS_8[1], "64");
        regs8mrm.put(Lexemes.REGISTERS_8[2], "5C");
        regs8mrm.put(Lexemes.REGISTERS_8[3], "7C");
        regs8mrm.put(Lexemes.REGISTERS_8[4], "4C");
        regs8mrm.put(Lexemes.REGISTERS_8[5], "6C");
        regs8mrm.put(Lexemes.REGISTERS_8[6], "54");
        regs8mrm.put(Lexemes.REGISTERS_8[7], "74");

        //TODO do better gen mod + reg + r/m
        regs32mrm.put(Lexemes.REGISTERS_32[0], "44");
        regs32mrm.put(Lexemes.REGISTERS_32[1], "5C");
        regs32mrm.put(Lexemes.REGISTERS_32[2], "4C");
        regs32mrm.put(Lexemes.REGISTERS_32[3], "54");
        regs32mrm.put(Lexemes.REGISTERS_32[4], "6C");
        regs32mrm.put(Lexemes.REGISTERS_32[5], "74");
        regs32mrm.put(Lexemes.REGISTERS_32[6], "7C");
        regs32mrm.put(Lexemes.REGISTERS_32[7], "64");
    }

    static String calcOpcodeDATATYPE(LexemesTable[] buffTable){
        String opcode = "";

        if(buffTable[2].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            opcode = Integer.toHexString(Integer.parseInt(buffTable[2].getLexeme()));
        }else if(buffTable[2].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
            opcode = Integer.toHexString(Integer.parseInt(buffTable[2].getLexeme().toUpperCase().replaceAll("B", ""), 2));
        }
        else if(buffTable[2].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){
            opcode = buffTable[2].getLexeme().toUpperCase().replaceAll("H", "");
        }
        else if(buffTable[2].getLexeme().equals(Lexemes.LITERALS[2]) && buffTable[3].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            opcode = Integer.toHexString(-1 * Integer.parseInt(buffTable[3].getLexeme()));
        }
        else if(buffTable[2].getLexeme().equals(Lexemes.LITERALS[1]) && buffTable[3].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            opcode = Integer.toHexString(Integer.parseInt(buffTable[3].getLexeme().toUpperCase().replace("+", "")));
        }
        else if(buffTable.length > 4){
            //TODO calculate expression
        }

        StringBuilder buff = new StringBuilder("");

        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[0])){
            int del = 2 - opcode.length();

            if(del < 0){
                return opcode.substring(Math.abs(del), opcode.length());
            }

            for(int i = 1; i <= del; i++){
                buff.append("0");
            }

            return buff.toString() + opcode;
        }
        else if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[1])){
            int del = 4 - opcode.length();

            if(del < 0){
                return opcode.substring(Math.abs(del), opcode.length());
            }

            for(int i = 1; i <= del; i++){
                buff.append("0");
            }

            return buff.toString() + opcode;
        }
        else if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[2])){
            int del = 8 - opcode.length();

            if(del < 0){
                return opcode.substring(Math.abs(del), opcode.length());
            }

            for(int i = 1; i <= del; i++){
                buff.append("0");
            }

            return buff.toString() + opcode;
        }


        return opcode;
    }

    static String calcOpcodeINC(LexemesTable[] buffTable){
        String opcode = "";

        if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[1])){
            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)){
                opcode = "FE ";
                opcode += Integer.toHexString(Integer.parseInt("11000" + regs8.get(buffTable[1].getLexeme().toUpperCase()), 2));
                return opcode;
            }
            else if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                opcode = Integer.toHexString(Integer.parseInt("01000" + regs32.get(buffTable[1].getLexeme().toUpperCase()), 2));
                return opcode;
            }
        }

        return opcode;
    }

    //TODO provide SEG: important !!!!
    static String calcOpcodeDEC(LexemesTable[] buffTable){
        String opcode = "FE 4C";

        if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[2])){
            opcode = Opcode.calcEfAdressOpcode(opcode, buffTable, 3);
        }

        return opcode;
    }

    static String calcOpcodeADD(LexemesTable[] buffTable){
        String opcode = "";

        if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[3])){
            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)){
                opcode = "02 ";
                String buff = "11" + regs8.get(buffTable[1].getLexeme().toUpperCase());
                opcode += Integer.toHexString(Integer.parseInt(buff + regs8.get(buffTable[3].getLexeme().toUpperCase()), 2));

                return opcode;
            } else if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                opcode = "03 ";
                String buff = "11" + regs32.get(buffTable[1].getLexeme().toUpperCase());
                opcode += Integer.toHexString(Integer.parseInt(buff + regs32.get(buffTable[3].getLexeme().toUpperCase()), 2));

                return opcode;
            }


        }

        return opcode;
    }

    //TODO provide SEG: important !!!!
    static String calcOpcodeOR(LexemesTable[] buffTable) {
        String opcode = "";

        if (buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[4])) {
            if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                opcode = "0B ";
                opcode += regs32mrm.get(buffTable[1].getLexeme().toUpperCase());
            } else if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)) {
                opcode = "0A ";
                opcode += regs8mrm.get(buffTable[1].getLexeme().toUpperCase());
            }

            opcode = calcEfAdressOpcode(opcode, buffTable, 3);
        }




        return opcode;
    }

    static String calcOpcodeAND(LexemesTable[] buffTable){
        String opcode = "";

        if (buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[5])) {
            int pos = 0;
            for(int i = 0; i < buffTable.length; i++){
                if(buffTable[i].getLexeme().equals(Lexemes.LITERALS[0])){
                    pos = i;
                    break;
                }
            }

            pos++;

            if(buffTable[pos].getLinkLexeme().equals(Lexemes.REGISTER_8)){
                opcode = "20 ";
                opcode += regs8mrm.get(buffTable[pos].getLexeme().toUpperCase());
            }
            else if(buffTable[pos].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                opcode = "21 ";
                opcode += regs32mrm.get(buffTable[pos].getLexeme().toUpperCase());
            }

            opcode = calcEfAdressOpcode(opcode, buffTable, 1);
        }

        return opcode;
    }

    static String calcOpcodeMOV(LexemesTable[] buffTable){
        String opcode = "";

        return opcode;
    }

    private static String calcEfAdressOpcode(String opcode, LexemesTable[] buffTable, int pos){
        String buff  = Integer.toHexString(Integer.parseInt("00" + regs32.get(buffTable[pos + 3].getLexeme().toUpperCase()) +
                regs32.get(buffTable[pos + 1].getLexeme().toUpperCase()), 2));

        if(buff.length() == 1){
            buff = "0" + buff;
        }

        opcode += buff;

        if(buffTable[pos + 4].getLexeme().toUpperCase().equals(Lexemes.LITERALS[1])){
            buff = Integer.toHexString(Integer.parseInt(buffTable[pos + 5].getLexeme()));
            if(buff.length() == 1){
                buff = "0" + buff;
            }
            opcode += " " + buff;
        }
        else{
            opcode += "  00";
        }

        return opcode.toUpperCase();
    }
}
