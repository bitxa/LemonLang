package org.example.symbols;

public class Token {
    public String type;
    public String value;

    public Token( String value, String type) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
