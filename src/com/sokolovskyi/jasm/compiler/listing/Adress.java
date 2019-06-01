package com.sokolovskyi.jasm.compiler.listing;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.util.Map;

final class Adress {

    private Adress(){}

    static String getStrAdress(int adress){
        String strAdress = "0000";

        switch (Integer.toHexString(adress).length()){
            case 1: strAdress = "000" + Integer.toHexString(adress).toUpperCase();
                break;

            case 2: strAdress = "00" + Integer.toHexString(adress).toUpperCase();
                break;

            case 3: strAdress = "0" +Integer.toHexString(adress).toUpperCase();
                break;

            case 4: strAdress = Integer.toHexString(adress).toUpperCase() + "";
        }

        return strAdress;
    }

    static int calcAdressDATATYPE(LexemesTable[] buffTable, int adress) {
        if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[0])) {
            adress += 0x1;
        } else if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[1])) {
            adress += 0x2;
        } else if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[2])) {
            adress += 0x4;
        }

        return adress;
    }

    //TODO dec word 66|
    static int calcAdressDEC(LexemesTable[] buffTable, int adress){
        adress += 0x3;

        if(buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[3].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])){
            adress += 0x1;
        }

        int pos = 0;
        while(!buffTable[pos].getLexeme().equals(Lexemes.LITERALS[4])) pos++;

        if(Opcode.is8BitNumber(buffTable[pos + 5])){
            adress += 0x1;
        }else{
            adress += 0x4;
        }


        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4])){
            adress += 0x1;
        }

        return adress;
    }

    static int calcAdressOR(LexemesTable[] buffTable, int adress){
        adress += 0x3;

        if(buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[3].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])){
            adress += 0x1;
        }

        int p = 0;
        while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4])) p++;

        if(Opcode.is8BitNumber(buffTable[p + 5])){
            adress += 0x1;
        }else{
            adress += 0x4;
        }

        return adress;
    }

    static int calcAdressAND(LexemesTable[] buffTable, int adress){
        adress += 0x3;

        if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[1].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])){
            adress += 0x1;
        }

        int p = 0;
        while(!buffTable[p].getLexeme().equals(Lexemes.LITERALS[4])) p++;

        if(Opcode.is8BitNumber(buffTable[p + 5])){
            adress += 0x1;
        }else{
            adress += 0x4;
        }


        return  adress;
    }

    static int calcAdressINC(LexemesTable[] buffTable, int adress){
        if(buffTable.length > 0){
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[1])){
                if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                    return adress + 0x1;
                }
                if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)){
                    return adress + 0x2;
                }
            }
        }

        return adress;
    }

    static int calcAdressADD(LexemesTable[] buffTable, int adress){
        if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[3])){
            return adress + 0x2;
        }

        return adress;
    }

    static int calcAdressMOV(LexemesTable[] buffTable, int adress){
        if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)){
            adress += 0x2;
        }
        else if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            adress += 0x5;
        }

        return adress;
    }


    static int calcAdressCMP(LexemesTable[] buffTable, int adress){
        adress += 0x3;

        if(buffTable[3].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[3].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[2])) {
            adress += 0x1;
        }


        if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4])){
            adress += 0x1;
        }

        int pos = 0;

        while(!buffTable[pos].getLexeme().equals(Lexemes.LITERALS[4]))pos++;

        if(Opcode.is8BitNumber(buffTable[pos + 5])){
            adress += 0x1;
        }else{
            adress += 0x4;
        }

        pos = 0;

        for(int i = 0; i < buffTable.length; i++){
            if(buffTable[i].getLexeme().equals(Lexemes.LITERALS[0])){
                pos = i;
                break;
            }
        }
        pos++;

        //TODO testing
        if(buffTable[pos].getLexeme().equals(Lexemes.LITERALS[2]) || buffTable[pos].getLexeme().equals(Lexemes.LITERALS[1])) pos++;

        if(Opcode.is8BitNumber(buffTable[pos])){
            adress += 0x1;
        }else{
            if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[3])){
                adress += 0x4;
            }else{
                adress += 0x2;
            }
        }

        return adress;
    }

    static int calcAdressJB(LexemesTable[] buffTable, Map<String, Integer[]> labelAdreses, int adress){
        if(labelAdreses.containsKey(buffTable[1].getLexeme().toUpperCase())){
            adress += 0x2;
        }else{
            adress += 0x6;
        }

        return adress;
    }

    static int calcAdressJMP(LexemesTable[] buffTable, Map<String, Integer[]> labelAdresses, int adress){

        if(buffTable[1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(labelAdresses.containsKey(buffTable[1].getLexeme().toUpperCase())){
                adress += 0x2;
            }else{
                adress += 0x5;
            }
        }else{
            if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_S) && !buffTable[1].getLexeme().toUpperCase().equals(Lexemes.REGISTERS_S[5])){
                adress += 0x1;
            }

            adress += 0x3;

            int pos = 0;
            while(!buffTable[pos].getLexeme().equals(Lexemes.LITERALS[4])){
                pos++;
            }

            if(Opcode.is8BitNumber(buffTable[pos + 5])){
                adress += 0x1;
            }else{
                adress += 0x4;
            }
        }

        return adress;
    }
}
