package org.example.symbols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenPattern {


    // * PATRONES
    private static final String KEYWORD_PATTERN = "\\b(vbl|lit|number|string|if|else|for|print)\\b";

    // Identificadores o nombres de variables
    private static final String IDENTIFIER_PATTERN = "\\b[A-Za-z][A-Za-z0-9]*\\b";

    // Operadores aritméticos
    private static final String ARITHMETIC_OPERATOR_PATTERN = "\\+|-|\\*|/|%";

    // Operadores relacionales o de comparación
    private static final String COMPARISON_OPERATOR_PATTERN = "<|<=|>|>=|==|!=";

    // Operadores lógicos &&: AND, ||: OR, !: NOT
    private static final String LOGICAL_OPERATOR_PATTERN = "&&|\\|\\||!";

    // Literales true: verdadero, false: falso
    private static final String BOOLEAN_LITERAL_PATTERN = "\\btrue\\b|\\bfalse\\b";

    // Operador de asignación
    private static final String ASSIGNMENT_OPERATOR_PATTERN = "=";

    // Cadenas de texto dentro de las comillas: "cadena"
    private static final String STRING_PATTERN = "\"[^\"]*\"";

    //Operadores de puntuación
    private static final String PUNCTUATION_PATTERN = "[(),{};]";

    //Decimales
    private static final String FLOAT_NUMBER_PATTERN = "\\d+\\.\\d+";

    //Enteros
    private static final String INTEGER_NUMBER_PATTERN = "\\d+";

    private final String pattern;


    TokenPattern(String pattern) {
        this.pattern = pattern;
    }

    //Compara el input con el patrón especificado
    private static boolean matches(char lexeme, String type){
        Pattern pattern = Pattern.compile(type);
        return pattern.matcher(String.valueOf(lexeme)).matches();
    }

}
