package syntacticAnalyzer;

import lexer.Token;

import java.util.List;

public class Parser {

    private List<Token> tokenList;

    public Parser(List<Token> tokens) {
        tokenList = tokens;
    }

}
