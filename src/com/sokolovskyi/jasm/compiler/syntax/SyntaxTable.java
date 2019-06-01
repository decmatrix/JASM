package com.sokolovskyi.jasm.compiler.syntax;

import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

public class SyntaxTable {
    private LexemesTable[] lexicalSentece;
    private SentenceTable sentence;
    private String line;

    public SyntaxTable(LexemesTable[] lexicalSentence, SentenceTable sentence, String line){
        this.lexicalSentece = lexicalSentence;
        this.sentence= sentence;
        this.line = line;
    }

    public LexemesTable[] getLexemesTable(){
        return lexicalSentece;
    }

    public SentenceTable getSentence(){
        return sentence;
    }
}
