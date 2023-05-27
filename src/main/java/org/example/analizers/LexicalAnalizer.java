package org.example.analizers;

import org.example.input_stream.StreamReader;
import org.example.symbols.Token;
import org.example.symbols.TokenPattern;
import org.example.symbols.TokenType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class LexicalAnalizer {
    StreamReader reader;
    TokenPattern pattern;

    ArrayList<String> tokens;

    ArrayList<Token> finalTokens;


    public LexicalAnalizer() throws FileNotFoundException {
        this.reader = new StreamReader("assets/code.txt");
        this.pattern = new TokenPattern();
        this.finalTokens = new ArrayList<>();
    }
    private boolean isWhitespace(String token) {
        return token.equals(" ");
    }
    public void collectTokens() throws IOException {
        String line;
        tokens = new ArrayList<>();

        while ((line = this.reader.buffer().readLine()) != null) {
            char[] chars = line.toCharArray();
            StringBuilder token = new StringBuilder();

            int state = 0;
            boolean foundNumber = false;
            boolean foundPossibleDecimal = false;
            for (int i = 0; i < chars.length ; i++) {
                String lexeme = valueOf(chars[i]);

                    switch (state) {
                        case 0:
                            foundPossibleDecimal = false;
                            //Letra
                            if (lexeme.matches("[A-Za-z]")) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }

                            state +=1;

                        case 1:
                            //Numeros
                            if(lexeme.matches("\\d+") || foundPossibleDecimal){
                                token.append(lexeme);
                                if (valueOf(chars[i+1]).equals(".")){
                                    foundPossibleDecimal = true;
                                    state = 1;
                                    continue;
                                }
                                state = 0;
                            }
                            state +=1;
                        case 2:
                            // Operadores aritméticos  + - * / %
                            if(lexeme.matches(TokenPattern.ARITHMETIC_OPERATOR_PATTERN)){
                                tokens.add(valueOf(token));
                                token = new StringBuilder();
                                token.append((lexeme));
                                state = 0;
                                continue;
                            }
                            state +=1;

                        case 3:
                            //Operadores lógicos  & | !
                            if(lexeme.matches(TokenPattern.LOGICAL_OPERATOR_PATTERN)){
                                tokens.add(valueOf(token));
                                tokens.add(lexeme);
                                token = new StringBuilder();
                                state = 0;
                                continue;
                            }
                            state +=1;
                        case 4:
                            //Operadores de comparación < <= > >= == !=
                            if(lexeme.matches(TokenPattern.COMPARISON_OPERATOR_PATTERN)){

                            }

                        default:
                            throw new IllegalStateException("Invalid input: " + state);
                    }
            }


                /*
                if (stringValue.matches(TokenPattern.PUNCTUATION_PATTERN)){
                    tokens.add(valueOf(token));
                    tokens.add(valueOf(lexeme));

                    token = new StringBuilder();
                    continue;
                }

                if (!isWhitespace(stringValue)){
                   token.append(lexeme);
                   continue;
                }

                tokens.add(String.valueOf(token));
                token = new StringBuilder(); */
        }

        //tokens.removeIf(str -> str.trim().isEmpty());
    }


    /*
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
     */

}
