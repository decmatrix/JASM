package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.Lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.listing.Listing;
import com.sokolovskyi.jasm.compiler.parser.ParserLexemesTables;
import com.sokolovskyi.jasm.compiler.parser.TextParser;
import com.sokolovskyi.jasm.compiler.parser.ParserSentencesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;

import java.io.*;
import java.util.ArrayList;

public class Compile implements Runnable{
    private String text;
    private String pathFile;

    //for result
    private ArrayList<LexemesTable[]> tablesOfLexemes;
    private SentenceTable[] tablesOfSentences;


    //array of errors
    private String[] errors;


    public Compile(String text, String pathToCompileFile){
        this.text = text;
        pathFile = pathToCompileFile;
    }

    private void createFile(){
        //FIXME file not created

        File file = new File(pathFile);

        if(!file.isFile()) try{
            if(!file.createNewFile()) throw new IOException("File wasn't created");
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void writeResultCompilation(){
        createFile();

        String[] lines = text.split("\n");

        try(FileWriter writer = new FileWriter(pathFile)){
            for(int i = 0; i < tablesOfLexemes.size(); i++){

                if(lines[i].equals("")) continue;
                lines[i] = lines[i].trim();

                writer.write("\n\n" + i + ") " + lines[i] + '\n');
                writer.write("=============================================\n");

                for(int j = 0; j < tablesOfLexemes.get(i).length; j++){
                    writer.write(j + ") " + tablesOfLexemes.get(i)[j] + "\n");
                }

                writer.write("=============================================\n");
                writer.write("Label: " + tablesOfSentences[i].getPosLabelName() + " | " +
                        "Mnemocode: " + tablesOfSentences[i].getPosLexemeMnemonic() + " (" +
                        tablesOfSentences[i].getCountLexemeMnemonic() + ") | " +
                        "First operand: " + tablesOfSentences[i].getPosFirstOperand() + " (" +
                        tablesOfSentences[i].getCountFirstOperand() + ") | " +
                        "Second operand: " + tablesOfSentences[i].getPosSecondOperand() + " (" +
                        tablesOfSentences[i].getCountSecondOperand() + ") | " + '\n');
                writer.write("=============================================\n");
            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initErrorsList(){

    }

    @Override
    public void run() {
        //get parsed text
        TextParser parser = new TextParser(text);
        ArrayList<String[]> parseList;
        parseList = parser.getParsedText();

        //create tables (Lexical analysis)
        tablesOfLexemes = new ArrayList<>();
        for (String[] strings : parseList) {
            tablesOfLexemes.add(ParserLexemesTables.getTablesFromLine(strings));
        }

        //create tables (Syntactic analysis)
        tablesOfSentences = ParserSentencesTable.getTablesFromLexemsTable(tablesOfLexemes);

        //TODO see in doc of course work


        //TODO create table of vars in asm


        //TODO parse lexical errors


        //TODO parse syntactic errors


        //create listing file
        Listing listing = new Listing(text, tablesOfLexemes, errors);

        //write in file
        writeResultCompilation();

        //for debugging
        //Debugger.outLSTables(tablesOfLexemes, tablesOfSentences);
    }
}
