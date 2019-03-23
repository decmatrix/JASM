package com.sokolovskyi.jasm.compiler.parser;

import java.util.ArrayList;

public class TextParser{
    private String[] text;

    public TextParser(String text){
        this.text = text.split("\n");
    }


    private String[] split(String text){
        ArrayList<String> lexems = new ArrayList<>();
        StringBuilder str = new StringBuilder();

        for(int i = 0; i < text.length(); i++){
            char ch = text.charAt(i);

            if(ch == ';'){
                break;
            }
            else if(ch == ' '){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }
            }else if(ch == ',') {
                if (str.length() > 0) {
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }
                lexems.add(",");
            }else if(ch == '['){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add("[");
            }else if(ch == ']'){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add("]");
            }else if(ch == '('){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add("(");
            }else if(ch == ')'){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add(")");
            }else if(ch == '+'){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add("+");
            }else if(ch == '-'){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add("-");
            }else if(ch == '*'){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add("*");
            }else if(ch == ':'){
                if(str.length() > 0){
                    lexems.add(str.toString().trim());
                    str.setLength(0);
                }

                lexems.add(":");
            } else{
                str.append(ch);
            }
        }

        if(str.length() > 0){
            lexems.add(str.toString().trim());
        }

        String[] parsedText = new String[lexems.size()];
        lexems.toArray(parsedText);
        return parsedText;
    }

    public ArrayList<String[]> getParsedText(){
        ArrayList<String[]> parsedText = new ArrayList<>();

        for(String str : text){
            parsedText.add(split(str));
        }

        return parsedText;
    }



}
