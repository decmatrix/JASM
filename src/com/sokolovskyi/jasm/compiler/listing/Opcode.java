package com.sokolovskyi.jasm.compiler.listing;

import com.sokolovskyi.jasm.compiler.additionals.CalcMathExpr;
import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.util.HashMap;
import java.util.Map;

final class Opcode {
    private Opcode(){}

    private static Map<String, String> regs32;
    private static Map<String, String> regs8;
    private static Map<String, String> regsSeg;

    private static Map<String, String> regs8mrm;
    private static Map<String, String> regs32mrm;

    //FIXME it is a no-go, rebuild and don't use static {} !!!
    static{
        regs32 = new HashMap<>();
        regs8 = new HashMap<>();
        regsSeg = new HashMap<>();

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

        regsSeg.put(Lexemes.REGISTERS_S[0], "2E:");
        regsSeg.put(Lexemes.REGISTERS_S[1], "64:");
        regsSeg.put(Lexemes.REGISTERS_S[2], "3E:");
        regsSeg.put(Lexemes.REGISTERS_S[3], "26:");
        regsSeg.put(Lexemes.REGISTERS_S[4], "65:");
        regsSeg.put(Lexemes.REGISTERS_S[5], "36:");

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

        boolean flag = true;

        if(buffTable.length > 4) {
            Integer res = CalcMathExpr.calcLinearExp(buffTable, 2);

            if (res == null) opcode = "0000";
            else {
                opcode = Integer.toHexString(res);
                flag = false;
            }
        }
        else if(buffTable[2].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            opcode = Integer.toHexString(Integer.parseInt(buffTable[2].getLexeme()));
        }
        else if(buffTable[2].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
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

        StringBuilder buffStr = new StringBuilder("");

        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[0])){
            int del = 2 - opcode.length();

            return getStringOpcode(opcode, false, buffStr, del);
        }
        else if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[1])){
            int del = 4 - opcode.length();

            return getStringOpcode(opcode, false, buffStr, del);
        }
        else if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[2])){
            int del = 8 - opcode.length();

            return getStringOpcode(opcode, flag, buffStr, del);
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

    static String calcOpcodeDEC(LexemesTable[] buffTable){
        String opcode = "";

        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4])){
            opcode += "66| ";
        }

        opcode += "FE ";

        int p = 0;
        while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4]))p++;

        if(is8BitNumber(buffTable[p + 5])) opcode += "4C";
        else opcode += "8C";

        if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[2])) {
            if(buffTable[3].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])){
                opcode = Opcode.calcEfAdressOpcode(opcode, buffTable, 5);
            }
            else if (buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S)) {
                opcode = regsSeg.get(buffTable[3].getLexeme().toUpperCase()) + " ";

                if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4])){
                    opcode += "66| ";
                }

                opcode += "FE 4C";
                opcode = Opcode.calcEfAdressOpcode(opcode, buffTable, 5);
            } else {
                opcode = Opcode.calcEfAdressOpcode(opcode, buffTable, 3);
            }
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


    static String calcOpcodeOR(LexemesTable[] buffTable) {
        String opcode = "";

        if (buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[4])) {

            if (buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[3].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])) {
                opcode = regsSeg.get(buffTable[3].getLexeme().toUpperCase()) + " ";
            }

            int p = 0;
            while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4]))p++;

            if(is8BitNumber(buffTable[p + 5])) {

                if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                    opcode += "0B ";
                    opcode += regs32mrm.get(buffTable[1].getLexeme().toUpperCase());
                } else if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)) {
                    opcode += "0A ";
                    opcode += regs8mrm.get(buffTable[1].getLexeme().toUpperCase());
                }

                if (buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S)) {
                    opcode = calcEfAdressOpcode(opcode, buffTable, 5);
                } else {
                    opcode = calcEfAdressOpcode(opcode, buffTable, 3);
                }
            }
            else{
                if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                    opcode += "0B ";

                    opcode += Integer.toHexString(Integer.parseInt(regs32mrm.get(buffTable[1].getLexeme().toUpperCase()), 16) + 64);
                } else if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)) {
                    opcode += "0A ";
                    opcode += Integer.toHexString(Integer.parseInt(regs8mrm.get(buffTable[1].getLexeme().toUpperCase()), 16) + 64);
                }

                if (buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S)) {
                    opcode = calcEfAdressOpcode(opcode, buffTable, 5);
                } else {
                    opcode = calcEfAdressOpcode(opcode, buffTable, 3);
                }
            }
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

            int p = 0;
            while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4]))p++;

            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[1].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])){
                opcode = regsSeg.get(buffTable[1].getLexeme().toUpperCase()) + " ";
            }

            if(is8BitNumber(buffTable[p + 5])) {

                if (buffTable[pos].getLinkLexeme().equals(Lexemes.REGISTER_8)) {
                    opcode += "20 ";
                    opcode += regs8mrm.get(buffTable[pos].getLexeme().toUpperCase());
                } else if (buffTable[pos].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                    opcode += "21 ";
                    opcode += regs32mrm.get(buffTable[pos].getLexeme().toUpperCase());
                }

            }else{
                if (buffTable[pos].getLinkLexeme().equals(Lexemes.REGISTER_8)) {
                    opcode += "20 ";
                    opcode += Integer.toHexString(Integer.parseInt(regs8mrm.get(buffTable[pos].getLexeme().toUpperCase()), 16) + 64);
                } else if (buffTable[pos].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                    opcode += "21 ";
                    opcode += Integer.toHexString(Integer.parseInt(regs32mrm.get(buffTable[pos].getLexeme().toUpperCase()), 16) + 64);
                }
            }

            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_S)) {
                opcode = calcEfAdressOpcode(opcode, buffTable, 3);
            }else{
                opcode = calcEfAdressOpcode(opcode, buffTable, 1);
            }
        }

        return opcode;
    }

    static String calcOpcodeMOV(LexemesTable[] buffTable){
        String opcode = "";
        if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[6])){

            //TODO not support
            if(buffTable[3].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_8[0])){
                    opcode = "A0 ";
                }
                else if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_32[0])){
                    opcode = "A1 ";
                }else if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTERS_8)){
                    opcode = "8A ";
                }else if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTERS_32)){
                    opcode = "8B ";
                }
            }else{
                if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)) {
                    opcode = Integer.toHexString(Integer.parseInt("10110" + regs8.get(buffTable[1].getLexeme().toUpperCase()), 2)) + " ";

                } else if (buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                    opcode = Integer.toHexString(Integer.parseInt("10111" + regs32.get(buffTable[1].getLexeme().toUpperCase()), 2)) + " ";
                }
            }

            String buff = "";

            if(buffTable[3].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                buff = Integer.toHexString(Integer.parseInt(buffTable[3].getLexeme()));
            }else if(buffTable[3].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                buff = Integer.toHexString(Integer.parseInt(buffTable[3].getLexeme().toUpperCase().replaceAll("B", ""), 2));
            }
            else if(buffTable[3].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){
                buff = buffTable[3].getLexeme().toUpperCase().replaceAll("H", "");
            }
            else if(buffTable[3].getLexeme().equals(Lexemes.LITERALS[2]) && buffTable[4].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                buff = Integer.toHexString(-1 * Integer.parseInt(buffTable[4].getLexeme()));
            }
            else if(buffTable[3].getLexeme().equals(Lexemes.LITERALS[1]) && buffTable[4].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                buff = Integer.toHexString(Integer.parseInt(buffTable[4].getLexeme().toUpperCase().replace("+", "")));
            }
            else if(buffTable.length > 4){
                //TODO calculate expression
            }

            StringBuilder buffer = new StringBuilder("");

            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)){
                int del = 2 - buff.length();

                return buildImmCom(opcode, buff, buffer, del);
            }
            else if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                int del = 8 - buff.length();

                return buildImmCom(opcode, buff, buffer, del);
            }
        }

        return opcode;
    }

    static String calcOpcodeCMP(LexemesTable[] buffTable){
        String opcode = "";

        int pos = 0;
        for(int i = 0; i < buffTable.length; i++){
            if(buffTable[i].getLexeme().equals(Lexemes.LITERALS[0])){
                pos = i;
                break;
            }
        }

        if(buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[3].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])){
            opcode = regsSeg.get(buffTable[3].getLexeme().toUpperCase()) + " ";
        }

        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4])){
            opcode += "66| ";
        }

        String buff = "100000";

        if(is8BitNumber(buffTable[pos + 1])){
            buff += "1";
        }else{
            buff += "0";
        }

        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[6])){
            buff += "0";
        }else{
            buff += "1";
        }

        opcode += Integer.toHexString(Integer.parseInt(buff, 2)) + " ";


        int p = 0;
        while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4])) p++;

        if(is8BitNumber(buffTable[p + 5])) opcode += "7C";
        else opcode += "BC";

        if(buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S)){
            opcode = calcEfAdressOpcode(opcode, buffTable, 5);
        }else{
            opcode = calcEfAdressOpcode(opcode, buffTable, 3);
        }

        opcode += " ";


        pos++;

        buff = "";
        if(buffTable[pos].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            buff = Integer.toHexString(Integer.parseInt(buffTable[pos].getLexeme()));
        }else if(buffTable[pos].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
            buff = Integer.toHexString(Integer.parseInt(buffTable[pos].getLexeme().toUpperCase().replaceAll("B", ""), 2));
        }
        else if(buffTable[pos].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){
            buff = buffTable[pos].getLexeme().toUpperCase().replaceAll("H", "");
        }
        else if(buffTable[pos].getLexeme().equals(Lexemes.LITERALS[2]) && buffTable[pos + 1].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            buff = Integer.toHexString(-1 * Integer.parseInt(buffTable[pos + 1].getLexeme()));
        }
        else if(buffTable[pos].getLexeme().equals(Lexemes.LITERALS[1]) && buffTable[pos + 1].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
            buff = Integer.toHexString(Integer.parseInt(buffTable[pos + 1].getLexeme().toUpperCase().replace("+", "")));
        }
        else if(buffTable.length > 4){
            //TODO calculate expression
        }

        //TODO testing
        if(buffTable[pos].getLexeme().equals(Lexemes.LITERALS[2]) || buffTable[pos].getLexeme().equals(Lexemes.LITERALS[1])) pos++;

        if(is8BitNumber(buffTable[pos])){
            int del = 2 - buff.length();

            if(del < 0){
                buff = buff.substring(Math.abs(del), buff.length());
            }


            StringBuilder buffer = new StringBuilder();
            for(int i = 1; i <= del; i++){
                buffer.append("0");
            }

            opcode += buffer.toString() + buff;
        }else {

            StringBuilder buffer = new StringBuilder("");

            if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[3])) {
                int del = 8 - buff.length();

                if (del < 0) {
                    opcode += buff.substring(Math.abs(del), buff.length());
                } else {
                    opcode = buildImmCom(opcode, buff, buffer, del);
                }
            } else if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4])) {
                int del = 4 - buff.length();

                if (del < 0) {
                    opcode += buff.substring(Math.abs(del), buff.length());
                } else {
                    opcode = buildImmCom(opcode, buff, buffer, del);
                }
            } else if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[6])) {
                int del = 2 - buff.length();

                opcode = buildImmCom(opcode, buff, buffer, del);
            }
        }


        return opcode;
    }

    static String calcOpcodeJB(LexemesTable[] buffTable, Map<String, Integer[]> labelAdreses, int[] adresses, int pos){
        String opcode = "";

        if(adresses[pos] <= labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0]){
            opcode = "OF 82 ";

            String buffAdress = "" + Integer.toHexString(labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0]);

            int del = 8 - buffAdress.length();

            if(del < 0){
                buffAdress = buffAdress.substring(Math.abs(del), opcode.length());
            }

            opcode += "0".repeat(Math.max(0, del)) + buffAdress + " R";
        }else{
            int nextAdr = 0;
            int i = pos;

            while(i < adresses.length){
                i++;

                if(adresses[i] != -1){
                    nextAdr = adresses[i];
                    break;
                }
            }

            int del = labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0] - nextAdr;

            if(del < -127 || del > 127) {
                opcode = "E9 ";

                String str = Integer.toHexString(labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0]);
                opcode += getStringOpcode(str, false, new StringBuilder(), 8 - str.length());

                opcode += " R";
            }else {

                opcode = "72 ";
                String buff = Integer.toHexString(del);
                opcode += buff.substring(buff.length() - 2, buff.length());
            }
        }

        return opcode;
    }

    static String calcOpcodeJMP(LexemesTable[] buffTable, Map<String, Integer[]> labelAdreses, int[] adresses, int pos){
        String opcode = "";

        if(buffTable[1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){

            int ladr = labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0];

            if(adresses[pos] < ladr){
                int del = ladr - adresses[pos] - 2;

                if(del < -127 || del > 127){
                    opcode = "E9 ";

                    String str = Integer.toHexString(labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0]);
                    opcode += getStringOpcode(str, false, new StringBuilder(), 8 - str.length());

                    opcode += " R";
                }else {

                    opcode = "EB ";

                    String buff = Integer.toHexString(del);

                    int delta = 2 - buff.length();
                    buff = "0".repeat(delta) + buff;

                    opcode += buff;
                    opcode += " 90 90 90";
                }
            }else{
                int nextAdr = 0;
                int i = pos;

                while(i < adresses.length){
                    i++;

                    if(adresses[i] != -1){
                        nextAdr = adresses[i];
                        break;
                    }
                }

                int del = labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0] - nextAdr;

                if(del < -127 || del > 127) {
                    opcode = "E9 ";

                    String str = Integer.toHexString(labelAdreses.get(buffTable[1].getLexeme().toUpperCase())[0]);
                    opcode += getStringOpcode(str, false, new StringBuilder(), 8 - str.length());

                    opcode += " R";
                }else {

                    String buff = Integer.toHexString(del);

                    opcode += buff.substring(buff.length() - 2, buff.length());
                }
            }


        }else{
            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[1].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[5])){
                opcode += regsSeg.get(buffTable[1].getLexeme().toUpperCase()) + " ";
            }

            opcode += "FF ";

            int p = 0;
            while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4])){
                p++;
            }

            if(is8BitNumber(buffTable[p + 5])){
                opcode += "64";
            }else{
                opcode += "A4";
            }

            opcode = calcEfAdressOpcode(opcode, buffTable, p);
        }

        return opcode;
    }

    private static String buildImmCom(String opcode, String buff, StringBuilder buffer, int del) {
        if(del < 0){
            return opcode + buff.substring(Math.abs(del), buff.length());
        }else {

            buffer.append("0".repeat(Math.max(0, del)));

            return opcode + buffer.toString() + buff;
        }
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
            if(is8BitNumber(buffTable[pos + 5])){
                if(buff.length() == 1){
                    buff = "0" + buff;
                }
            }else{
                int del = 8 - buff.length();
                buff = "0".repeat(Math.max(0, del)) + buff;
            }

            opcode += " " + buff;
        }
        else{
            opcode += "  00";
        }

        return opcode.toUpperCase();
    }

    public static boolean is8BitNumber(LexemesTable number){

        switch (number.getLinkLexeme()) {
            case Lexemes.DEC_CONSTANT: {
                int num = Integer.parseInt(number.getLexeme());
                return num >= -127 && num <= 127;
            }
            case Lexemes.BIN_CONSTANT: {
                String buff = number.getLexeme().toUpperCase();
                buff = buff.replaceAll("B", "");

                int num = Integer.parseInt(buff, 2);
                return num >= -127 && num <= 127;
            }
            case Lexemes.HEX_CONSTANT: {
                String buff = number.getLexeme().toUpperCase();
                buff = buff.replaceAll("H", "");

                int num = Integer.parseInt(buff, 16);
                return num >= -127 && num <= 127;
            }
        }

        return false;
    }

    private static String getStringOpcode(String opcode, boolean flag, StringBuilder buffStr, int del) {
        String buff;

        if(del < 0){
            buff = opcode.substring(Math.abs(del), opcode.length());
        }else{
            buffStr.append("0".repeat(Math.max(0, del)));
            buff = buffStr.toString() + opcode;
        }

        if(flag){
            StringBuilder buffer = new StringBuilder();
            StringBuilder res = new StringBuilder();

            for(int i = buff.length() - 2; i >= 0; i -= 2){
                buffer.append(buff.charAt(i));
                buffer.append(buff.charAt(i + 1));
            }

            return buffer.toString();
        }

        return buff;
    }
}
