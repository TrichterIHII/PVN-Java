package de.pvn;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class Statement {
    public Lexer.TOKEN_TYPE datatype;
    public Lexer.TOKEN_TYPE mod;
    public Token name;
    public Object value;
}

public class Parser {
    public ArrayList<Statement> allStatements = new ArrayList<>();

    int currentLine;
    int currentToken;

    private final Lexer lexer;

    public Parser(@NotNull Lexer lexer) {
        this.lexer = lexer;
        for (currentLine = 0; currentLine < lexer.allTokens.size(); currentLine++) {
            allStatements.add(parseStatement(lexer.allTokens.get(currentLine)));
        }
    }

    public Statement parseStatement(@NotNull ArrayList<Token> tokens) {
        // i.new test_int = 5
        currentToken = 0;
        Statement res = new Statement();

        // Datatype //i
        res.datatype = tokens.get(currentToken).type;
        currentToken++;

        // .
        if (tokens.get(currentToken).type != Lexer.TOKEN_TYPE.DOT) ErrorHandle.throwError(ErrorHandle.Error.UNEXPECTED_TOKEN, currentLine, currentToken, lexer.getUrl().toString());
        currentToken++;

        // Mod //
        Lexer.TOKEN_TYPE mod = tokens.get(currentToken).type;
        res.mod = mod;
        currentToken++;

        // Name & Error checks
        Token name = tokens.get(currentToken);
        if (name.type != Lexer.TOKEN_TYPE.INDENT) ErrorHandle.throwError(ErrorHandle.Error.NOT_VALID_NAME, currentLine, currentToken, lexer.getUrl().toString());
        List<Statement> sameNameStatements = allStatements.stream().filter(s -> s.name.equals(name)).toList();
        boolean existSameNameStatement = allStatements.stream().anyMatch(s -> s.name.equals(name));

        if (mod == Lexer.TOKEN_TYPE.NEW && existSameNameStatement) ErrorHandle.throwError(ErrorHandle.Error.ALREADY_EXISTING, currentLine, currentToken, lexer.getUrl().toString());
        if (mod == Lexer.TOKEN_TYPE.EDIT) {
            for (Statement s : sameNameStatements) {
                if (!existSameNameStatement) ErrorHandle.throwError(ErrorHandle.Error.NOT_EXISTING, currentLine, currentToken, lexer.getUrl().toString());
                // remove old statement
                allStatements.remove(s);
            }
        }
        res.name = name;
        currentToken++;

        // =
        if (tokens.get(currentToken).type != Lexer.TOKEN_TYPE.EQUAL) ErrorHandle.throwError(ErrorHandle.Error.UNEXPECTED_TOKEN, currentLine, currentToken, lexer.getUrl().toString());
        currentToken++;

        // Value
        res.value = tokens.get(currentToken).text;

        return res;
    }
}

