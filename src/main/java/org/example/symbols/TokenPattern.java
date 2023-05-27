package org.example.symbols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenPattern {


    // * PATRONES lexemas
    // Operadores aritméticos
    public static final String ARITHMETIC_OPERATOR_PATTERN = "\\+|-|\\*|/|%";

    // Operadores relacionales o de comparación
    public static final String COMPARISON_OPERATOR_PATTERN = "<|<=|>|>=|==|!=";

    // Operadores lógicos &&: AND, ||: OR, !: NOT
    public static final String LOGICAL_OPERATOR_PATTERN = "[&|!]";

    // Operador de asignación
    public static final String ASSIGNMENT_OPERATOR_PATTERN = "=";

    // Operadores de puntuación
    public static final String PUNCTUATION_PATTERN = "[(),{};]";

    // Enteros
    public static final String INTEGER_NUMBER_PATTERN = "[0-9]";

    // Letra
    public static final String LETTER_PATTERN = "[A-Za-z]";

    // Espacio en blanco
    public static final String WHITE_SPACE= " ";

    // Cadenas de texto, contenidas dentro de "  ".
    public static final String STRING_DOUBLE_QUOTE= "\"";

    // Punto, usado en los decimales
    public static final String DECIMAL_FLOAT_POINT =  "\\.";

    // Símbolo para insertar comentario
    public static final String COMMENT =  "$";



    // * PATRONES tokens
    // Cadenas de texto dentro de las comillas: "cadena"
    public static final String STRING_PATTERN = "\"[^\"]*\"";

    // Literales true: verdadero, false: falso
    public static final String BOOLEAN_LITERAL_PATTERN = "\\btrue\\b|\\bfalse\\b";

    // Palabras reservadas
    public static final String KEYWORD_PATTERN = "\\b(vbl|lit|number|string|if|else|for|print)\\b";

    // Identificadores o nombres de variables
    public static final String IDENTIFIER_PATTERN = "\\b[A-Za-z][A-Za-z0-9]*\\b";

    //Decimales
    public static final String FLOAT_NUMBER_PATTERN = "\\d+\\.\\d+";




}
