package model.utils;
import java.util.Objects;

public class Token {
    public enum Type {
        String, NegateString, And, Or
    }

    private Type type;
    private String value;

    public Token(Type type) {
        this.type = type;
        this.value = "";
    }

    public Token(String value) {
        if (value.contains("!")){
            this.type = Type.NegateString;
            this.value = value.replace("!", "");
        } else {
            this.type = Type.String;
            this.value = value;
        }
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}