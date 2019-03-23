package com.sokolovskyi.jasm.compiler.syntax.exceptions;

public class SyntacticException extends Exception {
    public SyntacticException(int numberLine, String reason){
        super("In " + numberLine + ": " + reason);
    }
}
