/**
 * Додаткові функції для позрахунків
 */
package com.sokolovskyi.jasm.compiler.additionals;

import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public final class CalcMathExpr {
    private CalcMathExpr(){}

    private static Map<Character, Integer> opPriority;

    private static Stack<Character> operators;
    private static Stack<Integer> operands;

    private static final char[] ops = {'+', '-', '*', '(', ')'};

    static{
        operators = new Stack<>();
        operands = new Stack<>();

        opPriority = new HashMap<>();

        opPriority.put('+', 2);
        opPriority.put('-', 2);
        opPriority.put('*', 1);
        opPriority.put('(', -1);
        opPriority.put(')', 3);
    }

    public static Integer calcLinearExp(LexemesTable[] buffTable, int pos){
        Integer res = null;

        //clear stacks before work
        freeStacks();

        String exp;
        StringBuilder buff = new StringBuilder();

        for(int i = pos; i < buffTable.length; i++){
            buff.append(buffTable[i].getLexeme());
        }

        exp = buff.toString();

        buff.setLength(0);
        char ch;

        for(int i = 0; i < exp.length(); i++){
            ch = exp.charAt(i);


            if(isOperator(ch)) {
                if (buff.length() == 0 && (ch == '-' || ch == '+')) operands.push(0);
                else if(buff.length() != 0){
                    operands.push(Integer.parseInt(buff.toString()));
                    buff.setLength(0);
                }


                if(!pushToStackOperators(ch)){
                    return null;
                }
            }
            else if(Character.isDigit(ch)){
                buff.append(ch);
            }else{
                return null;
            }
        }

        if(buff.length() != 0) operands.push(Integer.parseInt(buff.toString()));

        if(operators.empty() && operands.capacity() == 1){
            return operands.pop();
        }else if(!operators.empty() && !operands.empty()){
            if(operands.capacity() < 2) return null;

            int right;
            int left;
            Integer resBuff;

            while(!operators.empty()){
                right = operands.pop();
                left = operands.pop();

                resBuff = calcSimpleExp(left, right, operators.pop());
                if(resBuff == null) return null;

                operands.push(resBuff);
            }

            return operands.pop();
        }


        return res;
    }


    private static void freeStacks(){

        while(!operators.empty()) operators.pop();
        while(!operands.empty()) operands.pop();
    }

    private static boolean pushToStackOperators(char operator){
        if(Character.isDigit(operator) || Character.isLetter(operator)) return false;

        if(operators.empty()){
            operators.push(operator);
            return true;
        }

        int left;
        int right;
        Integer resBuff = 0;

        if(!isOperator(operator)) return false;

        if(operator == ')'){

            while(operators.peek() != '('){
                right = operands.pop();
                left = operands.pop();

                resBuff = calcSimpleExp(left, right, operators.pop());
                if(resBuff == null) return false;

                operands.push(resBuff);
            }

            System.out.println(resBuff);

            operators.pop();
            return true;
        }

        while(!operators.empty() && opPriority.get(operator) >= opPriority.get(operators.peek()) && operators.peek() != '('){
            right = operands.pop();
            left = operands.pop();


            resBuff = calcSimpleExp(left, right, operators.pop());
            if(resBuff == null) return false;

            operands.push(resBuff);
        }

        operators.push(operator);

        return true;
    }

    private static Integer calcSimpleExp(int left, int right, char operator){
        Integer res = null;

        switch(operator){
            case '+': return left + right;
            case '-': return left - right;
            case '*': return left * right;
        }

        return res;
    }

    private static boolean isOperator(char operator){

        for(char op : ops){
            if(op == operator) return true;
        }

        return false;
    }
}
