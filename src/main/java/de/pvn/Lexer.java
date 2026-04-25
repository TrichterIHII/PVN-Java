package de.pvn;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Token {
    public Lexer.TOKEN_TYPE type;
    public Object text;
    public Token(Lexer.TOKEN_TYPE type, Object text) {
        this.type = type;
        this.text = text;
    }
}

public class Lexer {

    enum TOKEN_TYPE {
        INDENT,
        DOT, EQUAL,
        EDIT, NEW,
        STRING, INT, FLOAT, BOOLEAN
    }

    public ArrayList<ArrayList<Token>> allTokens = new ArrayList<>();

    private final URL url;
    public Lexer(@NotNull URL path) throws FileNotFoundException, URISyntaxException {
        this.url = path;
        Scanner scanner = new Scanner(new File(path.toURI()));
        while (scanner.hasNextLine()) {
            allTokens.add(getTokens(scanner.nextLine()));
        }
    }

    ArrayList<Token> getTokens(@NotNull String line) {
        ArrayList<Token> res = new ArrayList<>();
        ArrayList<String> raw_tokens = new ArrayList<>(Arrays.asList(line.split(" ")));

        ArrayList<String> tokens = new ArrayList<>();

        for (String token : raw_tokens) {
            if (token.contains(".")) {
                String[] p = token.split("\\.");

                tokens.add(p[0]);
                tokens.add(".");
                tokens.add(p[1]);
            } else {
                tokens.add(token);
            }
        }

        for (String token : tokens) {
            switch (token) {
                // Datatypes
                case "s" -> res.add(new Token(TOKEN_TYPE.STRING, "s"));
                case "i" -> res.add(new Token(TOKEN_TYPE.INT, "i"));
                case "f" -> res.add(new Token(TOKEN_TYPE.FLOAT, "f"));
                case "b" -> res.add(new Token(TOKEN_TYPE.BOOLEAN, "b"));

                // Custom chars
                case "." -> res.add(new Token(TOKEN_TYPE.DOT, "."));
                case "=" -> res.add(new Token(TOKEN_TYPE.EQUAL, "="));

                // Modifiers
                case "new" -> res.add(new Token(TOKEN_TYPE.NEW, "new"));
                case "edit" -> res.add(new Token(TOKEN_TYPE.EDIT, "edit"));

                // Indent
                default -> res.add(new Token(TOKEN_TYPE.INDENT, token));
            }
        }

        return res;
    }

    public URL getUrl() {
        return url;
    }
}