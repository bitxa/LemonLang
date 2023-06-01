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
    boolean inString = false;
    boolean floatPointFoundBefore = false;

    boolean numberFoundBefore = false;

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
            for (int i = 0; i < chars.length; i++){
                String lexeme = valueOf(chars[i]);

                switch (state) {
                    case 0:
                        // Estado 0
                        if (lexeme.matches(TokenPattern.LETTER_PATTERN)) {
                            token.append(lexeme);
                            identifierOrReservedWordFound = true;
                            continue;
                        }

                        if (identifierOrReservedWordFound && lexeme.matches(TokenPattern.INTEGER_NUMBER_PATTERN)){
                            state = 11;
                        }
                        state += 1;

                    case 1:
                        // Numeros enteros
                        if (lexeme.matches(TokenPattern.INTEGER_NUMBER_PATTERN)) {
                            token.append(lexeme);
                            numberFoundBefore = true;
                            state = 1;
                            continue;
                        }

                        state = 10;

                    case 2:
                        // Operadores aritméticos  + - * / %
                        if (lexeme.matches(TokenPattern.ARITHMETIC_OPERATOR_PATTERN)) {
                            if (inString) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }

                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            floatPointFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            state = 0;
                            continue;
                        }
                        state +=1;

                    case 3:
                        // Operadores lógicos  & !
                        if (lexeme.matches(TokenPattern.LOGICAL_OPERATOR_PATTERN)) {
                            if (inString) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }

                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            state = 0;
                            floatPointFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 4:
                        // Operadores < > ! =
                        if (lexeme.matches(TokenPattern.COMPARISON_OPERATOR_PATTERN)) {
                            if (inString) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            token = new StringBuilder();
                            token.append(lexeme);
                            state = 5;
                            floatPointFoundBefore = false;
                            identifierOrReservedWordFound = false;
                            continue;
                        }
                        state += 2;

                    case 5:
                        // Operador =
                        if (lexeme.equals(TokenPattern.ASSIGNMENT_OPERATOR_PATTERN)) {
                            token.append(lexeme);
                            tokens.add(valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            continue;
                        }
                        tokens.add(valueOf(token));
                        token = new StringBuilder();

                        state = 0;

                    case 6:
                        // Espacio
                        if (lexeme.matches(TokenPattern.WHITE_SPACE)) {
                            if (inString) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            floatPointFoundBefore = false;
                            identifierOrReservedWordFound = false;

                            continue;
                        }
                        state += 1;

                    case 7:
                        // Operadores puntuación () {} , ;]
                        if (lexeme.matches(TokenPattern.PUNCTUATION_PATTERN)) {
                            if (inString) {
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                            tokens.add(valueOf(token));
                            tokens.add(lexeme);
                            token = new StringBuilder();
                            state = 0;
                            floatPointFoundBefore = false;
                            identifierOrReservedWordFound = false;
                            continue;
                        }
                        state += 1;

                    case 8:
                        // COMENTARIOS
                        if (lexeme.equals(TokenPattern.COMMENT) && !inString) {
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
                            if (inString) {
                                tokens.add(valueOf(token));
                                token = new StringBuilder();
                                inString = false;
                                state = 0;
                                continue;
                            }
                            floatPointFoundBefore = false;
                            identifierOrReservedWordFound = false;
                            inString = true;
                            state = 0;
                            continue;
                        }
                        state += 1;


                    case 10:
                        //Punto flotante
                        if (floatPointFoundBefore && !inString) {
                            throwError(line, "INVALID FLOAT POINT .");
                        }

                        if (lexeme.matches(TokenPattern.DECIMAL_FLOAT_POINT)) {
                            if(valueOf(chars[i-1]).matches(TokenPattern.INTEGER_NUMBER_PATTERN) &&
                                    valueOf(chars[i+1]).matches(TokenPattern.INTEGER_NUMBER_PATTERN)
                            ) {
                                token.append(lexeme);
                                state = 1;
                                floatPointFoundBefore = true;
                                numberFoundBefore = false;
                                continue;
                            }
                        }
                        state += 1;
                    case 11:
                        if(identifierOrReservedWordFound){
                            //Identificador o palabra reservada
                            if(lexeme.matches(TokenPattern.INTEGER_NUMBER_PATTERN)){
                                token.append(lexeme);
                                continue;
                            }
                            if(lexeme.matches(TokenPattern.LETTER_PATTERN)){
                                token.append(lexeme);
                                state = 0;
                                continue;
                            }
                        }
                        state += 1;

                    default:
                        // string cadena="resultado:.?$%?$?%";
                        if (inString) {
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
