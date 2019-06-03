package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.grammar.GrammarExcHandler;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.lexis.LexicalExcHandler;
import com.sokolovskyi.jasm.compiler.listing.Listing;
import com.sokolovskyi.jasm.compiler.parser.TableParser;
import com.sokolovskyi.jasm.compiler.parser.TextParser;
import com.sokolovskyi.jasm.compiler.semantics.SemanticExcHandler;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;
import com.sokolovskyi.jasm.compiler.syntax.SyntacticExcHandler;
import com.sokolovskyi.jasm.compiler.syntax.SyntaxTable;

import java.io.*;
import java.util.ArrayList;

public class Compile /*implements Runnable*/{
    private String text;
    private String pathFile;

    //for result
    private ArrayList<LexemesTable[]> tablesOfLexemes;
    private SentenceTable[] tablesOfSentences;
    private SyntaxTable[] syntaxTables;


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
        errors = new String[tablesOfLexemes.size() + 1];

        for(int i = 0; i < errors.length; i++){
            errors[i] = null;
        }
    }


    public void compile() {
        //get parsed text
        TextParser parser = new TextParser(text);
        ArrayList<String[]> parseList;
        parseList = parser.getParsedText();

        //create tables (Lexical analysis)
        tablesOfLexemes = new ArrayList<>();
        for (String[] strings : parseList) {
            tablesOfLexemes.add(TableParser.getLexemeTables(strings));
        }

        //create tables (Syntactic analysis)
        tablesOfSentences =  TableParser.getSentenceTables(tablesOfLexemes);

        //create syntax table
        syntaxTables = TableParser.getSyntaxTables(tablesOfLexemes, tablesOfSentences, text.split("\n"));

        //TODO see in doc of course work

        //init list errors
        initErrorsList();

        //parse lexical errors
        LexicalExcHandler.catchLexicalExceptions(errors, tablesOfLexemes);

        //parse syntactics errors
        SyntacticExcHandler.catchException(errors, syntaxTables);

        //parse semantic errors
        SemanticExcHandler.catchException(errors, syntaxTables);

        //parse grammar errors
        GrammarExcHandler.catchException(errors, tablesOfLexemes, text.split("\n"));

        //create listing file
        Listing listing = new Listing(text, tablesOfLexemes, errors);

        //write in file
        writeResultCompilation();

        //for debugging
        //Debugger.outLSTables(tablesOfLexemes, tablesOfSentences);
    }
}
