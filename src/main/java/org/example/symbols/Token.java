package org.example.symbols;

public class Token{
    private final String value;
    private final String type;

    public Token(String value, String type){
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
