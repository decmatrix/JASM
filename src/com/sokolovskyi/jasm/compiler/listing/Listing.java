package com.sokolovskyi.jasm.compiler.listing;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Listing {
    private final String pathListing = "./src/tmp/list.lst";
    private String[] sourceCodeArr;
    private ArrayList<LexemesTable[]> tablesOfLexemes;
    private String[] errors;

    private int countErrors;

    public Listing(String sourceCode, ArrayList<LexemesTable[]> tablesOfLexemes, String[] errors){
        sourceCodeArr = sourceCode.split("\n");

        this.tablesOfLexemes = tablesOfLexemes;
        this.errors = errors;

        initListing();
    }

    private void initListing(){

        try(FileWriter writer = new FileWriter(pathListing);){

            writer.write("\t\t\tJASM Java Assembly Compiler v.0.3 beta \t\t" +  new Date() + '\n');
            writer.write("\t\t\t\tby Sokolovskyi Bohdan FAM KPI KV-73\n\n");

            //write in Listing
            writeMachineCommands(writer);
            writeSegmentTable(writer);
            writeIdTable(writer);

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }


    //TODO rebuild logic of listing formation
    private void writeMachineCommands(FileWriter writer) throws IOException{
        int adress = 0x0;
        LexemesTable[] buffTable;
        String opcode;

        countErrors = 0;

        for(int i = 0; i < sourceCodeArr.length; i++){
            //errors
            if(errors[i] != null){
                writer.write("0000 " + "??? " + sourceCodeArr[i] + errors[i] + '\n');
                countErrors++;
                continue;
            }

            //str of source code contains empty string
            if(sourceCodeArr[i].trim().equals("")){
                writer.write("     " + sourceCodeArr[i] + '\n');
                continue;
            }

            //TODO END
            if(tablesOfLexemes.get(i)[0].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[2])){
                writer.write("     " + sourceCodeArr[i] + '\n');
                continue;
            }

            //start machine commands
            buffTable = tablesOfLexemes.get(i);

            //SEGMENT , ENDS
            if(buffTable.length > 1) {
                if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[0]) ||
                        buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[1])) {
                    writer.write(Adress.getStrAdress(adress) + "   " + sourceCodeArr[i] + '\n');
                    adress = 0x0;
                    continue;
                }
            }

            //data types
            if(buffTable.length > 2) {
                //data types
                if (buffTable[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) &&
                        buffTable[1].getLinkLexeme().equals(Lexemes.DATATYPE)) {
                    opcode = Opcode.calcOpcodeDATATYPE(buffTable);
                    writer.write(Adress.getStrAdress(adress) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                    adress = Adress.calcAdressDATATYPE(buffTable, adress);
                }
            }

            //inc
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[1])){
                opcode = Opcode.calcOpcodeINC(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressINC(buffTable, adress);
                continue;
            }

            //dec
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[2])){
                opcode = Opcode.calcOpcodeDEC(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressDEC(buffTable, adress);
                continue;
            }

            //add
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[3])){
                opcode = Opcode.calcOpcodeADD(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressADD(buffTable, adress);
                continue;
            }

            //cli
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[0])){
                opcode = "FA";
                writer.write(Adress.getStrAdress(adress) + " " + opcode + " " + sourceCodeArr[i] + '\n');
                adress += 0x1;
                continue;
            }

            //or
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[4])){
                opcode = Opcode.calcOpcodeOR(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressOR(buffTable, adress);
                continue;
            }

            //and
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[5])){
                opcode = Opcode.calcOpcodeAND(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressAND(buffTable, adress);
                continue;
            }

            //mov
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[6])){
                opcode = Opcode.calcOpcodeMOV(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressMOV(buffTable, adress);
                continue;
            }

            //cmp
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[7])){
                opcode = Opcode.calcOpcodeCMP(buffTable);
                writer.write(Adress.getStrAdress(adress) + " " + opcode.toUpperCase() + " " + sourceCodeArr[i] + '\n');
                adress = Adress.calcAdressCMP(buffTable, adress);
                continue;
            }
        }

        //count errors
        writer.write("\t" + countErrors + " Errors\n");
    }

    private void writeSegmentTable(FileWriter writer) throws IOException{

    }

    private void writeIdTable(FileWriter writer)throws IOException{

    }
}
