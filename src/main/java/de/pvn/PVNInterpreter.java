package de.pvn;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PVNInterpreter {

    private final Parser parser;

    public PVNInterpreter(URL file) throws FileNotFoundException, URISyntaxException {
        Lexer lexer = new Lexer(file);
        parser = new Parser(lexer);
    }

    public void grabPvn(@NotNull Object obj) {
        HashMap<String, Object> variables = getPvn();
        AtomicReference<ArrayList<Lexer.TOKEN_TYPE>> datatypes = new AtomicReference<>(new ArrayList<>());
        parser.allStatements.stream()
                .filter(s -> s.datatype != null)
                .forEach(s -> datatypes.get().add(s.datatype));

        variables.forEach((s, o) -> {
            try {
                Field field = obj.getClass().getDeclaredField(s);

                parser.allStatements.stream()
                        .filter(statement -> statement.name.text.toString().equals(s))
                        .findFirst()
                        .ifPresent(statement -> {
                            try {
                                switch (statement.datatype) {
                                    case INT -> field.set(obj, Integer.parseInt(o.toString()));
                                    case FLOAT -> field.set(obj, Float.parseFloat(o.toString()));
                                    case BOOLEAN -> field.set(obj, Boolean.parseBoolean(o.toString()));
                                    case STRING -> {
                                        if (!(o.toString().toCharArray()[0] == '"' || o.toString().toCharArray()[o.toString().toCharArray().length] == '"'))
                                            ErrorHandle.throwError(ErrorHandle.Error.UNEXPECTED_TOKEN, 0, 0, "");
                                        field.set(obj, o.toString().substring(1, o.toString().length() - 1));
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        });
    }

    public HashMap<String, Object> getPvn() {
        HashMap<String, Object> res = new HashMap<>();

        parser.allStatements.stream()
                .filter(s -> s.name != null && !s.name.text.toString().isEmpty())
                .forEach(s -> res.put(s.name.text.toString(), s.value));

        return res;
    }
}
