package org.example.symbols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenPattern {


    // * PATRONES
    public static final String KEYWORD_PATTERN = "\\b(vbl|lit|number|string|if|else|for|print)\\b";

    // Identificadores o nombres de variables
    public static final String IDENTIFIER_PATTERN = "\\b[A-Za-z][A-Za-z0-9]*\\b";

    // Operadores aritméticos
    public static final String ARITHMETIC_OPERATOR_PATTERN = "\\+|-|\\*|/|%";

    // Operadores relacionales o de comparación
    public static final String COMPARISON_OPERATOR_PATTERN = "<|<=|>|>=|==|!=";

    // Operadores lógicos &&: AND, ||: OR, !: NOT
    public static final String LOGICAL_OPERATOR_PATTERN = "&&|\\|\\||!";

    // Literales true: verdadero, false: falso
    public static final String BOOLEAN_LITERAL_PATTERN = "\\btrue\\b|\\bfalse\\b";

    // Operador de asignación
    public static final String ASSIGNMENT_OPERATOR_PATTERN = "=";

    // Cadenas de texto dentro de las comillas: "cadena"
    public static final String STRING_PATTERN = "\"[^\"]*\"";

    //Operadores de puntuación
    public static final String PUNCTUATION_PATTERN = "[(),{};]";

    //Decimales
    public static final String FLOAT_NUMBER_PATTERN = "\\d+\\.\\d+";

    //Enteros
    public static final String INTEGER_NUMBER_PATTERN = "\\d+";

    //Compara el input con el patrón especificado
    public static boolean matches(char lexeme, String type){
        Pattern pattern = Pattern.compile(type);
        return pattern.matcher(String.valueOf(lexeme)).matches();
    }

}
