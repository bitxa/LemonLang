package org.example.analizers;

import org.example.input_stream.StreamReader;
import org.example.symbols.TokenPattern;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class LexicalAnalizer {
    StreamReader reader;
    TokenPattern pattern;
    ArrayList<String> tokens;
    String line;
    boolean stringQuoteFoundBefore = false;
    boolean floatPointNumberFoundBefore = false;

    boolean identifierOrReservedWordFound = false;



    public LexicalAnalizer() throws FileNotFoundException {
        this.reader = new StreamReader("assets/code.txt");

        this.pattern = new TokenPattern();
        tokens = new ArrayList<>();
    }

    public void collectTokens() throws Exception {
        fileLoop:
        while ((line = this.reader.buffer().readLine()) != null) {
            char[] chars = line.toCharArray();
            StringBuilder token = new StringBuilder();

            int state = 0;
            //aa ajdsjadjda 1a232
            for (int i = 0; i < chars.length; i++) {
                String lexeme = valueOf(chars[i]);

                switch (state) {
                    case 0:
                        // Palabras reservadas, identificadores
                        if (lexeme.matches(TokenPattern.LETTER_PATTERN)) {
                            token.append(lexeme);
                            identifierOrReservedWordFound = true;
                            continue;
                        }
                        state += 1;

                    case 1:
                        // Numeros enteros
                        if (lexeme.matches(TokenPattern.INTEGER_NUMBER_PATTERN)) {
                            token.append(lexeme);
                            state = 1;
                            continue;
                        }
                        if (lexeme.matches(TokenPattern.LETTER_PATTERN) && identifierOrReservedWordFound) {
                            token.append(lexeme);
                            state = 0;
                            continue;
                        }
                        state += 1;

                    case 2:
                        // Operadores aritméticos  + - * / %
                        if (lexeme.matches(TokenPattern.ARITHMETIC_OPERATOR_PATTERN)) {
                            if (stringQuoteFoundBefore) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            state = 0;
                            continue;
                        }
                        state += 1;

                    case 3:
                        // Operadores lógicos  & | !
                        if (lexeme.matches(TokenPattern.LOGICAL_OPERATOR_PATTERN)) {
                            if (stringQuoteFoundBefore) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            state = 0;
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 4:
                        // Operadores de comparación < <= > >= == !=
                        if (lexeme.matches(TokenPattern.COMPARISON_OPERATOR_PATTERN)) {
                            if (stringQuoteFoundBefore) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            state = 0;
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 5:
                        // Espacio
                        if (lexeme.matches(TokenPattern.WHITE_SPACE)) {
                            if (stringQuoteFoundBefore) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 6:
                        // Operadores puntuación () {} , ;]
                        if (lexeme.matches(TokenPattern.PUNCTUATION_PATTERN)) {
                            if (stringQuoteFoundBefore) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            state = 0;
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 7:
                        // Operador asignación =
                        if (lexeme.matches(TokenPattern.ASSIGNMENT_OPERATOR_PATTERN)) {
                            if (stringQuoteFoundBefore) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            state = 0;
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 8:
                        // COMENTARIOS
                        if (lexeme.equals(TokenPattern.COMMENT) && !stringQuoteFoundBefore) {
                            if (token.toString().length() == 0) {
                                continue fileLoop;
                            }

                            state += 1;
                        }
                        state += 1;
                    case 9:
                        // Comilla doble de cadenas de texto "
                        if (lexeme.matches(TokenPattern.STRING_DOUBLE_QUOTE)) {
                            token.append(lexeme);
                            if (stringQuoteFoundBefore) {
                                tokens.add(valueOf(token));
                                token = new StringBuilder();
                                stringQuoteFoundBefore = false;
                                state = 0;
                                continue;
                            }
                            floatPointNumberFoundBefore = false;
                            identifierOrReservedWordFound = false;
                            stringQuoteFoundBefore = true;
                            state = 0;
                            continue;
                        }
                        state += 1;

                    case 10:
                        //Punto flotante
                        if (floatPointNumberFoundBefore && !stringQuoteFoundBefore) {
                            throwError(line, "INVALID FLOAT POINT .");
                        }

                        if (lexeme.matches(TokenPattern.DECIMAL_FLOAT_POINT) && !identifierOrReservedWordFound) {
                            token.append(lexeme);
                            state = 1;
                            floatPointNumberFoundBefore = true;
                            continue;
                        }
                        state += 1;


                    default:
                        // string cadena="resultado:.?$%?$?%";
                        if (stringQuoteFoundBefore) {
                            token.append(lexeme);
                            state = 0;
                            continue;
                        }
                        throwError(line, "Symbol { " + lexeme + " } not valid");
                        break;
                }
                System.out.println(lexeme + " " + state);

            }
        }
    }

    public static void main(String[] args) throws Exception {
        LexicalAnalizer lexicalAnalizer = new LexicalAnalizer();
        lexicalAnalizer.collectTokens();

        //remueve los tokens que son espacios en blanco
        lexicalAnalizer.tokens.removeIf(str -> str.trim().isEmpty());
        for (String x : lexicalAnalizer.tokens) {
            System.out.println("TOKEN: " + x);
        }
    }

    public void throwError(String line, String error) {
        System.out.println(line);
        System.out.printf("%s ERROR: %s %s%n", "\u001B[31m", error, "\u001B[0m");
        System.exit(0);
    }
}
