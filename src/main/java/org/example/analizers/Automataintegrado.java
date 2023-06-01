package org.example.analizers;

import org.example.input_stream.StreamReader;
import org.example.symbols.TokenPattern;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;

public class Automataintegrado {

    private static StreamReader reader;

    private static ArrayList<String> tokens;
    private static String line;

    private static List<Character> OperadoresAritmeticos = Arrays.asList('+', '-', '%', '*', '/');
    private static List<Character> PuntationSymbols = Arrays.asList('{', '}', '[', ']', ';', ',', '(', ')');
    private static List<Character> RelationalOperators = Arrays.asList('<', '>', '=', '!');

    public static void main(String[] args) throws Exception {
        reader = new StreamReader("assets/code.txt");
        tokens = new ArrayList<String>();
        analizer();
    }

    public static void analizer() throws Exception {

        fileLoop:
        while ((line = reader.buffer().readLine()) != null) {
            char[] chars = line.toCharArray();
            int state = 0;

            int currentIndex = 0;
            boolean isSeparator = false;
            boolean isPointFloat = false;
            StringBuilder token = new StringBuilder();
            while (currentIndex < chars.length && state != -1) {
                // currentIndex += 1;
                isSeparator = false;
                char currentChar = chars[currentIndex];

                switch (state) {
                    case 0:
                        if (Character.isWhitespace(currentChar)) {
                            break;
                        } else if (Character.isLetter(currentChar)) {
                            token.append(currentChar);
                            state = 1;
                            break;
                        } else if (Character.isDigit(currentChar)) {
                            token.append(currentChar);
                            state = 3;
                            break;
                        } else if (RelationalOperators.contains(currentChar)) {
                            token.append(currentChar);
                            state = 8;
                            break;
                        } else if (currentChar == '"') {
                            token.append(currentChar);
                            state = 12;
                            break;
                        } else {
                            if (OperadoresAritmeticos.contains(currentChar)
                                    || currentChar == '&' || currentChar == '|'
                                    || PuntationSymbols.contains(currentChar)) {
                                token.append(currentChar);
                                tokens.add(valueOf(token));
                                System.out.println("Lexeme: " + valueOf(token));
                                token = new StringBuilder();
                                break;
                            } else if (currentChar == '$') {
                                continue fileLoop;
                            } else {
                                state = -1;
                                throwError(line, "Symbol { " + currentChar + " } not valid");
                                break fileLoop;
                            }
                        }

                    case 1:
                        if (Character.isLetter(currentChar)) {
                            token.append(currentChar);
                            break;
                        } else if (Character.isDigit(currentChar)) {
                            token.append(currentChar);
                            state = 2;
                        } else {
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            isSeparator = true;
                        }
                        break;

                    case 2:
                        if (Character.isDigit(currentChar)) {
                            token.append(currentChar);
                            break;
                        } else if (Character.isLetter(currentChar)) {
                            token.append(currentChar);
                            state = 1;
                        } else {
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            isSeparator = true;
                        }
                        break;

                    case 3:
                        if (Character.isDigit(currentChar)) {
                            token.append(currentChar);
                            break;
                        } else if (currentChar == '.') {
                            state = 4;
                            isSeparator = true;
                        } else if (Character.isLetter(currentChar)) {
                            state = -1;
                            throwError(line, "Symbol { " + currentChar + " } not valid");
                            break;
                            // System.out.println("error");
                        } else {
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            isSeparator = true;
                        }
                        break;

                    case 4:
                        if (currentChar == '.') {
                            token.append(currentChar);
                            state = 5;
                            isPointFloat = true;
                            break;
                        } else {
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            isSeparator = true;
                        }
                        break;

                    case 5:
                        if (isPointFloat && !Character.isDigit(currentChar)) {
                            state = -1;
                            throwError(line, "Symbol { " + currentChar + " } not valid");
                        } else if (Character.isDigit(currentChar)) {
                            token.append(currentChar);
                            isPointFloat = false;
                            break;
                        } else {
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                            isSeparator = true;
                        }
                        break;

                    case 8:
                        if (currentChar == '=') {
                            token.append(currentChar);
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                        } else {
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            isSeparator = true;
                        }
                        state = 0;
                        break;

                    case 12:
                        if (currentChar == '"') {
                            token.append(currentChar);
                            tokens.add(valueOf(token));
                            System.out.println("Lexeme: " + valueOf(token));
                            token = new StringBuilder();
                            state = 0;
                        } else {
                            token.append(currentChar);
                        }
                        break;
                    default:
                        state = -1;
                        throwError(line, "Symbol { " + currentChar + " } not valid");
                        break;
                }

                if (!isSeparator) {
                    currentIndex += 1;
                }
            }
            if (token.length() > 0) {
                tokens.add(valueOf(token));
                System.out.println("Lexeme: " + valueOf(token));
            }

        }
        System.out.println(tokens);
    }

    public static void throwError(String line, String error) {
        System.out.println(line);
        System.out.printf("%s ERROR: %s %s%n", "\u001B[31m", error, "\u001B[0m");
        System.exit(0);
    }
}
