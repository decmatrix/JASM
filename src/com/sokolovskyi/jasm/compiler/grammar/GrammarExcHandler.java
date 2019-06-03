package com.sokolovskyi.jasm.compiler.grammar;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.util.ArrayList;

public final class GrammarExcHandler {
    private GrammarExcHandler() {
    }


    public static void catchException(String[] errors, ArrayList<LexemesTable[]> table, String[] source) {
        int code = 0;
        int data = 0;

        for(int i = 0; i < table.size(); i++){
            if(source[i].trim().equals("")) continue;
            if(table.get(i).length <= 1) continue;

            if(table.get(i)[0].getLexeme().toUpperCase().equals("CODE") && table.get(i)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                code++;

                if(code > 1){
                    errors[i] = "\n(" + (i + 1) + ") error: " + GrammarErrors.Ox2;

                    for(int j = i; j < table.size(); j++){

                        if(table.get(j).length > 1 && (table.get(j)[0].getLexeme().toUpperCase().equals("DATA") || table.get(j)[0].getLexeme().toUpperCase().equals("CODE")) && table.get(j)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                            break;
                        }

                        if(table.get(i).length >= 1) errors[j] = "\n(" + (j + 1) + ") error: " + GrammarErrors.Ox1;
                    }
                }
            }
            if(table.get(i)[0].getLexeme().toUpperCase().equals("DATA") && table.get(i)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                data++;

                if(data > 1){
                    errors[i] = "\n(" + (i + 1) + ") error: " + GrammarErrors.Ox2;

                    for(int j = i; j < table.size(); j++){

                        if(table.get(j).length > 1 && (table.get(j)[0].getLexeme().toUpperCase().equals("DATA") || table.get(j)[0].getLexeme().toUpperCase().equals("CODE")) && table.get(j)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                            break;
                        }

                        if(table.get(i).length >= 1) errors[j] = "\n(" + (j + 1) + ") error: " + GrammarErrors.Ox1;
                    }
                }
            }

        }

        if(code == 0 && data == 0){
            for(int j = 0; j < table.size(); j++){
                if(source[j].trim().equals("")) continue;

                errors[j] = "\n(" + (j + 1) + ") error: " + GrammarErrors.Ox1;
            }

            return;
        }

        boolean existEnd = false;

        for(int i = 0; i < table.size(); i++){
            if(source[i].trim().equals("")) continue;

            if(table.get(i).length >= 1 && table.get(i)[0].getLexeme().toUpperCase().equals("END")) existEnd = true;

        }

        if(!existEnd){
            errors[table.size()] = "\n(" + (table.size() + 1) + ") error: " + GrammarErrors.Ox4;
        }

        int coundEnd = 0;
        int p = 0;

        for(int i = 0; i < table.size(); i++){
            if(source[i].trim().equals("")) continue;

            if(table.get(i).length >= 1 && table.get(i)[0].getLexeme().toUpperCase().equals("END")) {
                coundEnd++;

                if(coundEnd > 1) {
                    break;
                }

                p = i;
            }
        }

        if(coundEnd > 1){
            errors[p] =  "\n(" + (p + 1) + ") error: " + GrammarErrors.Ox3;

            for(int i = p + 1; i < table.size(); i++){
                errors[i] = "\n(" + (i + 1) + ") error: " + GrammarErrors.Ox1;
            }
        }

        for(int i = 0; i < table.size(); i++){
            if(source[i].trim().equals("")) continue;

            if(errors[i] != null) continue;

            if(table.get(i).length > 1 && table.get(i)[0].getLexeme().toUpperCase().equals("CODE") && table.get(i)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                int pos = i;
                boolean end = false;

                for(int j = i; j < table.size(); j++){
                    if(source[j].trim().equals("")) continue;
                    if(table.get(j).length <= 1) continue;

                    if(table.get(j)[0].getLexeme().toUpperCase().equals("CODE") && table.get(j)[1].getLexeme().toUpperCase().equals("ENDS")){
                        end = true;
                        i = j + 1;
                    }
                }

                if(!end){
                    for(int j = pos; j < table.size(); j++){
                        if(source[j].trim().equals("")) continue;

                        if(table.get(j).length > 1 && table.get(j)[0].getLexeme().toUpperCase().equals("DATA") && table.get(j)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                            break;
                        }

                        errors[j] = "\n(" + (j + 1) + ") error: " + GrammarErrors.Ox1;
                    }

                    return;
                }
            }

            if(table.get(i).length > 1 && table.get(i)[0].getLexeme().toUpperCase().equals("DATA") && table.get(i)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                int pos = i;
                boolean end = false;

                for(int j = i; j < table.size(); j++){
                    if(source[j].trim().equals("")) continue;
                    if(table.get(j).length <= 1) continue;

                    if(table.get(j)[0].getLexeme().toUpperCase().equals("DATA") && table.get(j)[1].getLexeme().toUpperCase().equals("ENDS")){
                        end = true;
                        i = j + 1;
                    }
                }

                if(!end){
                    for(int j = pos; j < table.size(); j++){
                        if(source[j].trim().equals("")) continue;

                        if(table.get(j).length > 1 && table.get(j)[0].getLexeme().toUpperCase().equals("CODE") && table.get(j)[1].getLexeme().toUpperCase().equals("SEGMENT")){
                            break;
                        }

                        errors[j] = "\n(" + (j + 1) + ") error: " + GrammarErrors.Ox1;
                    }

                    return;
                }
            }

            if(table.get(i).length >= 1 && !table.get(i)[0].getLexeme().toUpperCase().equals("END")) errors[i] = "\n(" + (i + 1) + ") error: " + GrammarErrors.Ox1;
        }
    }

}

