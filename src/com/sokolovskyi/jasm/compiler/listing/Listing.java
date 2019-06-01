package com.sokolovskyi.jasm.compiler.listing;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Listing {
    private final String pathListing = "./src/listings/list.lst";
    private String[] sourceCodeArr;
    private ArrayList<LexemesTable[]> tablesOfLexemes;
    private String[] errors;

    private Map<String, Integer[]>labelAdress;
    private Map<String, Integer[]>segmentAdresses;
    private Map<String, Integer[]>varsAdresses;
    private int[] adresses;

    public Listing(String sourceCode, ArrayList<LexemesTable[]> tablesOfLexemes, String[] errors){
        sourceCodeArr = sourceCode.split("\n");

        this.tablesOfLexemes = tablesOfLexemes;
        this.errors = errors;

        initListing();
    }

    private void initListing(){

        try(FileWriter writer = new FileWriter(pathListing)){

            writer.write("\t\t\tJASM Java Assembly Compiler v.0.3 beta \t\t" +  new Date() + '\n');
            writer.write("\t\t\t\tby Sokolovskyi Bohdan FAM KPI KV-73\n\n");

            //calc labels adress
            getLabelsAdreses();

            //write in Listing
            writeMachineCommands(writer);
            writeSegmentTable(writer);
            writeIdTable(writer);

            //count errors

            writer.write("\n\n\t\t\t" + countErrors() + " Errors\n");

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initAdresses(){
        if(adresses == null) return;

        for(int adr : adresses){
            adr = -1;
        }
    }

    private void getLabelsAdreses(){
        int adress = 0x0;

        LexemesTable[] buffTable;

        labelAdress = new LinkedHashMap<>();
        segmentAdresses = new LinkedHashMap<>();
        varsAdresses = new LinkedHashMap<>();

        adresses = new int[sourceCodeArr.length + 1];

        initAdresses();

        for(int i = 0; i < sourceCodeArr.length; i++){
            if(errors[i] != null){
                continue;
            }

            if(sourceCodeArr[i].trim().equals("")){
                continue;
            }

            //start machine commands
            buffTable = tablesOfLexemes.get(i);

            //SEGMENT , ENDS
            if(buffTable.length > 1) {
                if(buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[1])){
                    adresses[i] = adress;

                    Integer[] pair = {adress, i};
                    segmentAdresses.put(buffTable[0].getLexeme().toUpperCase(), pair);
                }

                if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[0])) {
                    adress = 0x0;

                    adresses[i] = adress;

                    continue;
                }
            }

            //data types, label
            if(buffTable.length >= 1) {
                //data types
                if (buffTable[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) &&
                        buffTable[1].getLinkLexeme().equals(Lexemes.DATATYPE)) {
                    adresses[i] = adress;

                    Integer[] pair = {adress, i};
                    varsAdresses.put(buffTable[0].getLexeme().toUpperCase(), pair);

                    adress = Adress.calcAdressDATATYPE(buffTable, adress);

                    continue;
                }

                //label
                if(buffTable[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) &&
                        buffTable[1].getLexeme().equals(Lexemes.LITERALS[8])){

                    adresses[i] = adress;

                    Integer[] pair = {adress, i};
                    labelAdress.put(buffTable[0].getLexeme().toUpperCase(), pair);
                    continue;
                }
            }



            //inc
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[1])){

                adresses[i] = adress;
                adress = Adress.calcAdressINC(buffTable, adress);

                continue;
            }

            //dec
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[2])){

                adresses[i] = adress;
                adress = Adress.calcAdressDEC(buffTable, adress);

                continue;
            }

            //add
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[3])){

                adresses[i] = adress;
                adress = Adress.calcAdressADD(buffTable, adress);

                continue;
            }

            //cli
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[0])){
                adresses[i] = adress;
                adress += 0x1;
                continue;
            }

            //or
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[4])){
                adresses[i] = adress;
                adress = Adress.calcAdressOR(buffTable, adress);
                continue;
            }

            //and
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[5])){
                adresses[i] = adress;
                adress = Adress.calcAdressAND(buffTable, adress);
                continue;
            }

            //mov
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[6])){
                adresses[i] = adress;
                adress = Adress.calcAdressMOV(buffTable, adress);
                continue;
            }

            //cmp
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[7])){
                adresses[i] = adress;
                adress = Adress.calcAdressCMP(buffTable, adress);
                continue;
            }

            //jb
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[8])){
                adresses[i] = adress;
                adress = Adress.calcAdressJB(buffTable, labelAdress, adress);
                continue;
            }

            //jmp
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[9])){
                adresses[i] = adress;
                adress = Adress.calcAdressJMP(buffTable, labelAdress, adress);
                continue;
            }

        }
    }


    private void writeMachineCommands(FileWriter writer) throws IOException{
        int adress = 0x0;
        LexemesTable[] buffTable;
        String opcode;

        for(int i = 0; i < sourceCodeArr.length; i++){
            //errors
            if(errors[i] != null){
                writer.write("0000 " + sourceCodeArr[i] + errors[i] + '\n');
                continue;
            }

            //str of source code contains empty string
            if(sourceCodeArr[i].trim().equals("")){
                writer.write("    " + sourceCodeArr[i] + '\n');
                continue;
            }

            if(tablesOfLexemes.get(i)[0].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[2])){
                writer.write("     " + sourceCodeArr[i] + '\n');
                continue;
            }

            //start machine commands
            buffTable = tablesOfLexemes.get(i);

            //SEGMENT
            if(buffTable.length > 1) {
                if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[0])) {
                    writer.write(Adress.getStrAdress(adresses[i]) + "   " + sourceCodeArr[i] + '\n');
                    continue;
                }

                if( buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[1])){
                    writer.write(Adress.getStrAdress(adresses[i]) + "   " + sourceCodeArr[i] + '\n');
                    continue;
                }
            }



            //data types, label
            if(buffTable.length >= 1) {
                //data types
                if (buffTable[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) &&
                        buffTable[1].getLinkLexeme().equals(Lexemes.DATATYPE)) {
                    opcode = Opcode.calcOpcodeDATATYPE(buffTable);
                    writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                }

                //label
                if(buffTable[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) &&
                        buffTable[1].getLexeme().equals(Lexemes.LITERALS[8])){
                    writer.write(Adress.getStrAdress(adresses[i]) + "  " + sourceCodeArr[i] + '\n');
                }
            }

            //inc
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[1])){
                opcode = Opcode.calcOpcodeINC(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //dec
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[2])){
                opcode = Opcode.calcOpcodeDEC(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //add
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[3])){
                opcode = Opcode.calcOpcodeADD(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //cli
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[0])){
                opcode = "FA";
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //or
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[4])){
                opcode = Opcode.calcOpcodeOR(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //and
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[5])){
                opcode = Opcode.calcOpcodeAND(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //mov
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[6])){
                opcode = Opcode.calcOpcodeMOV(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //cmp
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[7])){
                opcode = Opcode.calcOpcodeCMP(buffTable);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //jb
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[8])){
                opcode = Opcode.calcOpcodeJB(buffTable, labelAdress, adresses, i);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                continue;
            }

            //jmp
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[9])){
                opcode = Opcode.calcOpcodeJMP(buffTable, labelAdress, adresses, i);
                writer.write(Adress.getStrAdress(adresses[i]) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
            }
        }
    }

    private void writeSegmentTable(FileWriter writer) throws IOException{
        writer.write("\n\n\n\n\t\t\t\tN a m e\t\t\t\t  Size    Length\n\n");

        for(Map.Entry<String, Integer[]> entry : segmentAdresses.entrySet()){
            String segStr = entry.getKey() + " .".repeat(16);

            writer.write(segStr + "  " + "32 Bit" + "  " + Adress.getStrAdress(entry.getValue()[0]) + '\n');
        }
    }

    private void writeIdTable(FileWriter writer)throws IOException{
        writer.write("\n\n\n\n\t\t\t\tN a m e\t\t\t\t  Value    Attr\n\n");

        int maxSize = maxSizeId();

        for(Map.Entry<String, Integer[]> entry : labelAdress.entrySet()){
            writer.write(getFullIdStr(entry, maxSize));
        }

        writer.write("\n\n");

        for(Map.Entry<String, Integer[]> entry : varsAdresses.entrySet()){
            writer.write(getFullIdStr(entry, maxSize));
        }
    }

    private String getFullIdStr(Map.Entry<String, Integer[]> entry, int maxSize){
        String res;

        String labStr = getIdStr(entry.getKey(), maxSize);

        String attr = "UNKNOWN";


        if(entry.getValue()[1] <= segmentAdresses.get("DATA")[1]){
            attr = "DATA";
        }else if(entry.getValue()[1] <= segmentAdresses.get("CODE")[1]){
            attr = "CODE";
        }

        return labStr + "  " + Adress.getStrAdress(entry.getValue()[0]) + "    " + attr + '\n';
    }

    private String getIdStr(String segment , int max){
        String res = "";

        int del = max - segment.length();

        res = segment + " ".repeat(del) + " .".repeat(20 - max);

        return res;
    }

    private int countErrors(){
        int res = 0;

        for(String err : errors){
            if(err != null) res++;
        }

        return res;
    }

    private int maxSizeId(){
        int res = 0;

        for(Map.Entry<String, Integer[]> entry : labelAdress.entrySet()){
            if(entry.getKey().length() > res) res = entry.getKey().length();
        }

        for(Map.Entry<String, Integer[]> entry : varsAdresses.entrySet()){
            if(entry.getKey().length() > res) res = entry.getKey().length();
        }

        return res;
    }
}
