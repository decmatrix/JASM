package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.Debugger;
import com.sokolovskyi.jasm.compiler.Lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.Lexis.exceptions.LexicalExcHandler;
import com.sokolovskyi.jasm.compiler.Lexis.exceptions.LexicalException;
import com.sokolovskyi.jasm.compiler.listing.Listing;
import com.sokolovskyi.jasm.compiler.parser.ParserLexemesTables;
import com.sokolovskyi.jasm.compiler.parser.TextParser;
import com.sokolovskyi.jasm.compiler.parser.ParserSentencesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;
import com.sokolovskyi.jasm.compiler.syntax.exceptions.SyntacticExcHandler;

import java.io.*;
import java.util.ArrayList;

public class Compile implements Runnable{
    private String text;
    private String pathFile;

    //for result
    private ArrayList<LexemesTable[]> tablesOfLexemes;
    private SentenceTable[] tablesOfSentences;


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

                writer.write("\n\n" + lines[i] + '\n');
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

        //catch lexical exception
        //TODO come up with return exception in IDE
        try {
            LexicalExcHandler.catchException(tablesOfLexemes);
        }catch (LexicalException e){
          //TODO clean this line
          System.exit(1);
        }

        //create tables (Syntactic analysis)
        tablesOfSentences = ParserSentencesTable.getTablesFromLexemsTable(tablesOfLexemes);

        //create listing file
        Listing listing = new Listing();

        //write in file
        writeResultCompilation();

        //for debugging
        //Debugger.outLSTables(tablesOfLexemes, tablesOfSentences);
    }
}
