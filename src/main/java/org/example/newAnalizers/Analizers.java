package org.example.newAnalizers;

import org.example.input_stream.StreamReader;

import java.io.IOException;
import java.util.*;

public class Analizers {
    static StreamReader reader;
    private static List<String> lines;
    private static int currentLineIndex;
    private static int currentCharIndex;

    public static void main(String[] args) throws IOException {
        System.out.println("\u001B[32m" + "Leyendo archivo ... " + "\u001B[0m");
        reader = new StreamReader("assets/code.txt");
        System.out.println("\u001B[32m" + "Archivo leido." + "\u001B[0m");
        //
        lines = new ArrayList<>();
        String line;
        while ((line = reader.buffer().readLine()) != null) {
            lines.add(line);
        }
        currentLineIndex = 0;
        currentCharIndex = 0;
        //
        Parser();
    }

    public static String getTokenLexicalAnalizer() throws IOException {
        /* Separadores de los identificadores y palabras reservadas */
        List<Character> separators1 = Arrays.asList(' ', ';', '{', '=', '(', '>', '<', '!', '+', '-', '*', '/', ')');
        /* Separador de un número entero y decimal */
        List<Character> separators2 = Arrays.asList(' ', ';', '+', '-', '*', '/', ')');
        /* Separadores de simbolos de agrupación */
        List<Character> separators3 = Arrays.asList(' ', ';', '{');
        /* Separadores de otros simbolos ';' */
        List<Character> separators4 = Arrays.asList(' ', '}', '(');
        /* Palabras Reservadas */
        List<String> reservedWords = Arrays.asList("lemonade", "number", "while", "if", "else");

        /*simbolos*/
        List<Character> symbolsAgrupation = Arrays.asList('{', '}', '(', ')');
        List<Character> symbolsAritmetic = Arrays.asList('+', '-', '*', '/');
        List<Character> symbolsRelational = Arrays.asList('>', '<', '!', '=');
        List<Character> symbolsOthers = Arrays.asList(';');

        if (currentLineIndex >= lines.size()) {
            return null; // No hay más líneas
        }
        String currentLine = lines.get(currentLineIndex);
        if (currentCharIndex >= currentLine.length()) {
            // Se ha alcanzado el final de la línea actual, pasa a la siguiente línea
            currentLineIndex++;
            currentCharIndex = 0;
            return getTokenLexicalAnalizer(); // Recursivamente obtenemos el token de la siguiente línea
        }

        StringBuilder token = new StringBuilder();
        char currentChar = currentLine.charAt(currentCharIndex);
        int state = 0;

        /* Start analizer */
        while (true) {
            // System.out.println(state);

            switch (state) {
                case 0:
                    // System.out.println(state);
                    if (Character.isWhitespace(currentChar)) {
                        currentCharIndex++;
                        currentChar = currentLine.charAt(currentCharIndex);
                        break;
                    } else if (Character.isLetter(currentChar)) {
                        state = 1;
                    } else if (Character.isDigit(currentChar)) {
                        state = 3;
                    } else if (symbolsAritmetic.contains(currentChar)) {
                        state = 6;
                    } else if (symbolsRelational.contains(currentChar)) {
                        state = 7;
                    } else if (symbolsAgrupation.contains(currentChar)) {
                        state = 9;
                    } else if (symbolsOthers.contains(currentChar)) {
                        state = 10;
                    } else if (currentChar == '$') {
                        // Ignora lo que queda de la línea
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return getTokenLexicalAnalizer(); // Recursivamente obtenemos el token de la siguiente línea
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 1:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    // System.out.println("currentChar: " + currentChar +  " currentCharIndex " + currentCharIndex);
                    if (Character.isLetter(currentChar)) {
                        state = 1;
                    } else if (Character.isDigit(currentChar)) {
                        state = 2;
                    } else if (separators1.contains(currentChar) || Character.isWhitespace(currentChar)) {
                        if (reservedWords.contains(token.toString())) {
                            // System.out.println("reservedWords: " + token.toString());
                            return token.toString();
                        } else {
                            // System.out.println("ID: " + token.toString());
                            return "ID";
                        }
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 2:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (Character.isDigit(currentChar)) {
                        state = 2;
                    } else if (Character.isLetter(currentChar)) {
                        state = 1;
                    } else if (separators1.contains(currentChar) || Character.isWhitespace(currentChar)) {
                        // System.out.println("ID: " + token.toString());
                        return "ID";
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 3:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (Character.isDigit(currentChar)) {
                        state = 3;
                    } else if (currentChar == '.') {
                        state = 4;
                    } else if (separators2.contains(currentChar) || Character.isWhitespace(currentChar)) {
                        // System.out.println("NUM: " + token.toString());
                        return "NUM";
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 4:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (Character.isDigit(currentChar)) {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 5:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (Character.isDigit(currentChar)) {
                        state = 5;
                    } else if (separators2.contains(currentChar) || Character.isWhitespace(currentChar)) {
                        //System.out.println("NUM: " + token.toString());
                        return "NUM";
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 6:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (Character.isWhitespace(currentChar) || Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                        return token.toString();
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 7:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (currentChar == '=') {
                        state = 8;
                    } else if (Character.isWhitespace(currentChar) || Character.isLetter(currentChar) || Character.isDigit(currentChar)) { // separators
                        return token.toString();
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 8:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (Character.isWhitespace(currentChar) || Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                        return token.toString();
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 9:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (separators3.contains(currentChar) || Character.isWhitespace(currentChar) || Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                        return token.toString();
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                case 10:
                    currentCharIndex++;
                    if (currentCharIndex >= currentLine.length()) {
                        currentLineIndex++;
                        currentCharIndex = 0;
                        // System.out.println("Token: " + token.toString());
                        return token.toString();
                    }
                    currentChar = currentLine.charAt(currentCharIndex);
                    if (separators4.contains(currentChar) || Character.isWhitespace(currentChar) || Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                        // System.out.println("Token: " + token.toString());
                        return token.toString();
                    } else {
                        state = -1;
                    }
                    token.append(currentChar);
                    break;
                default:
                    throwError(currentLine, "Symbol { " + currentChar + " } not valid");
                    return null;
            }
        }
        /* End analizer */
    }

    public static void throwError(String line, String error) {
        System.out.println(line);
        System.out.printf("%s ERROR: %s %s%n", "\u001B[31m", error, "\u001B[0m");
        System.exit(0);
    }

    public static void Parser() throws IOException {
        System.out.println("\u001B[32m" + "Start Parser... " + "\u001B[0m");
        /* Start analizer */
        String token = getTokenLexicalAnalizer();
        System.out.println(token);
        // Obtiene el primer token
        // Continúa obteniendo tokens hasta que ya no haya más
        Stack<Integer> stateStack = new Stack<>();
        Stack<String> symbolStack = new Stack<>();
        stateStack.push(0);

        while (token != null) {
            Integer currentState = stateStack.peek();
            String currentToken = token;

            String action = getAction(currentState, currentToken);
            System.out.println("currentToken: " + currentToken);
            System.out.println("action: " + action);

            if (action.startsWith("d")) {
                Integer nextState = Integer.parseInt(action.substring(1));
                stateStack.push(nextState);
                symbolStack.push(currentToken);

                // get nexttoke form lexical analizer
                token = getTokenLexicalAnalizer();
            } else if (action.startsWith("r")) {
                //reducir
                String production = productions[Integer.parseInt(action.substring(1))];
                String[] parts = production.split("\\|");

                String left = parts[0].trim();
                String right = parts[1].trim();

                int count = right.split(" ").length;
                for (int i = 0; i < count; i++) {
                    stateStack.pop();
                    symbolStack.pop();
                }

                symbolStack.push(left);
                currentToken = symbolStack.peek();
                currentState = stateStack.peek();

                stateStack.push(getGoTo(currentState, currentToken));
            }
        }
        /* End analizer */
        System.out.println("\u001B[32m" + "End Parser." + "\u001B[0m");
    }

    private static String getAction(int state, String token) {
        for (String[] actionRow : actionTable) {
            int actionState = Integer.parseInt(actionRow[0]);
            String actionToken = actionRow[1];
            if (actionState == state && actionToken.equals(token)) {
                return actionRow[2];
            }
        }
        throw new RuntimeException("Error en el token " + token);
    }

    private static int getGoTo(int state, String token) {
        for (String[] gotoRow : gotoTable) {
            int gotoState = Integer.parseInt(gotoRow[0]);
            String gotoToken = gotoRow[1];
            if (gotoState == state && gotoToken.equals(token)) {
                return Integer.parseInt(gotoRow[2]);
            }
        }
        throw new RuntimeException("Error en el token " + token);
    }

    static String[][] actionTable = {{"0", "lemonade", "d2"}, {"1", "$", "acep"}, {"2", "ID", "d4"}, {"4", "{", "d5"}, {"5", "ID", "d13"}, {"5", "number", "d16"}, {"5", "while", "d14"}, {"5", "if", "d15"}, {"6", "}", "d17"}, {"7", "ID", "d13"}, {"7", "}", "r3"}, {"7", "number", "d16"}, {"7", "while", "d14"}, {"7", "if", "d15"}, {"8", "ID", "r4"}, {"8", "}", "r4"}, {"8", "number", "r4"}, {"8", "while", "r4"}, {"8", "if", "r4"}, {"9", "ID", "r5"}, {"9", "}", "r5"}, {"9", "number", "r5"}, {"9", "while", "r5"}, {"9", "if", "r5"}, {"10", "ID", "r6"}, {"10", "}", "r6"}, {"10", "number", "r6"}, {"10", "while", "r6"}, {"10", "if", "r6"}, {"11", "ID", "d13"}, {"11", "}", "r7"}, {"11", "number", "r7"}, {"11", "while", "r7"}, {"11", "if", "r7"}, {"12", "ID", "d13"}, {"13", "=", "d20"}, {"14", "(", "d21"}, {"15", "(", "d22"}, {"16", "ID", "r9"}, {"17", "$", "r1"}, {"18", "}", "r2"}, {"19", "ID", "r8"}, {"19", "}", "r8"}, {"19", "number", "r8"}, {"19", "while", "r8"}, {"19", "if", "r8"}, {"20", "ID", "d27"}, {"20", "(", "d26"}, {"20", "NUM", "d28"}, {"21", "ID", "d30"}, {"22", "ID", "d30"}, {"23", ";", "d32"}, {"23", "+", "d33"}, {"23", "-", "d34"}, {"24", ";", "r23"}, {"24", ")", "r23"}, {"24", "+", "r23"}, {"24", "-", "r23"}, {"24", "*", "d35"}, {"24", "/", "d36"}, {"25", ";", "r26"}, {"25", "+", "r26"}, {"25", "-", "r26"}, {"25", "*", "r26"}, {"25", "/", "r26"}, {"26", "ID", "d27"}, {"26", "(", "d26"}, {"26", "NUM", "d28"}, {"27", ";", "r28"}, {"27", ")", "r28"}, {"27", "+", "r28"}, {"27", "-", "r28"}, {"27", "*", "r28"}, {"27", "/", "r28"}, {"28", ";", "r29"}, {"28", ")", "r29"}, {"28", "+", "r29"}, {"28", "-", "r29"}, {"28", "*", "r29"}, {"28", "/", "r29"}, {"29", ")", "d38"}, {"30", "<", "d40"}, {"30", ">", "d41"}, {"30", "==", "d42"}, {"30", "<=", "d43"}, {"30", ">=", "d44"}, {"30", "!=", "d45"}, {"31", ")", "d46"}, {"32", "ID", "r10"}, {"32", "}", "r10"}, {"32", "number", "r10"}, {"32", "while", "r10"}, {"32", "if", "r10"}, {"33", "ID", "d27"}, {"33", "(", "d26"}, {"33", "NUM", "d28"}, {"34", "ID", "d27"}, {"34", "(", "d26"}, {"34", "NUM", "d28"}, {"35", "ID", "d27"}, {"35", "(", "d26"}, {"35", "NUM", "d28"}, {"36", "ID", "d27"}, {"36", "(", "d26"}, {"36", "NUM", "d28"}, {"37", ")", "d51"}, {"37", "+", "d33"}, {"37", "-", "d34"}, {"38", "{", "d52"}, {"39", "ID", "d53"}, {"40", "ID", "r15"}, {"41", "ID", "r16"}, {"42", "ID", "r17"}, {"43", "ID", "r18"}, {"44", "ID", "r19"}, {"45", "ID", "r20"}, {"46", "{", "d54"}, {"47", ";", "r21"}, {"47", ")", "r21"}, {"47", "+", "r21"}, {"47", "-", "r21"}, {"47", "*", "d35"}, {"47", "/", "d36"}, {"48", ";", "r22"}, {"48", ")", "r22"}, {"48", "+", "r22"}, {"48", "-", "r22"}, {"48", "*", "d35"}, {"48", "/", "d36"}, {"49", ";", "r24"}, {"49", ")", "r24"}, {"49", "+", "r24"}, {"49", "-", "r24"}, {"49", "*", "r24"}, {"49", "/", "r24"}, {"50", ";", "r25"}, {"50", ")", "r25"}, {"50", "+", "r25"}, {"50", "-", "r25"}, {"50", "*", "r25"}, {"50", "/", "r25"}, {"51", ";", "r27"}, {"51", ")", "r27"}, {"51", "+", "r27"}, {"51", "-", "r27"}, {"51", "*", "r27"}, {"51", "/", "r27"}, {"52", "ID", "d13"}, {"52", "number", "d16"}, {"52", "while", "d14"}, {"52", "if", "d15"}, {"53", ")", "r14"}, {"54", "ID", "d13"}, {"54", "number", "d16"}, {"54", "while", "d14"}, {"54", "if", "d15"}, {"55", "}", "d57"}, {"56", "}", "d58"}, {"57", "ID", "r11"}, {"57", "}", "r11"}, {"57", "number", "r11"}, {"57", "while", "r11"}, {"57", "if", "r11"}, {"58", "ID", "r12"}, {"58", "}", "r12"}, {"58", "number", "r12"}, {"58", "while", "r12"}, {"58", "if", "r12"}, {"58", "else", "d59"}, {"59", "{", "d60"}, {"60", "ID", "d13"}, {"60", "number", "d16"}, {"60", "while", "d14"}, {"60", "if", "d15"}, {"61", "}", "d62"}, {"62", "ID", "r13"}, {"62", "}", "r13"}, {"62", "number", "r13"}, {"62", "while", "r13"}, {"62", "if", "r13"},};
    static String[][] gotoTable = {{"0", "<P>", "1"}, {"5", "<BS>", "6"}, {"5", "<US>", "7"}, {"5", "<UD>", "8"}, {"5", "<ASIG>", "9"}, {"5", "<WHILE>", "10"}, {"5", "<IF>", "11"}, {"5", "<Tipo>", "12"}, {"7", "<BS>", "6"}, {"7", "<US>", "7"}, {"7", "<UD>", "8"}, {"7", "<ASIG>", "9"}, {"7", "<WHILE>", "10"}, {"7", "<IF>", "11"}, {"7", "<Tipo>", "12"}, {"12", "<ASIG>", "19"}, {"20", "<E>", "23"}, {"20", "<T>", "24"}, {"20", "<F>", "25"}, {"21", "<COND>", "29"}, {"22", "<COND>", "31"}, {"26", "<E>", "37"}, {"26", "<T>", "24"}, {"26", "<F>", "25"}, {"30", "<OP_REL>", "39"}, {"33", "<T>", "47"}, {"33", "<F>", "25"}, {"34", "<T>", "48"}, {"34", "<F>", "25"}, {"35", "<F>", "49"}, {"36", "<F>", "50"}, {"52", "<BS>", "55"}, {"52", "<US>", "7"}, {"52", "<UD>", "8"}, {"52", "<ASIG>", "9"}, {"52", "<WHILE>", "10"}, {"52", "<IF>", "11"}, {"52", "<Tipo>", "12"}, {"54", "<BS>", "56"}, {"54", "<US>", "7"}, {"54", "<UD>", "8"}, {"54", "<ASIG>", "9"}, {"54", "<WHILE>", "10"}, {"54", "<IF>", "11"}, {"54", "<Tipo>", "12"}, {"60", "<BS>", "61"}, {"60", "<US>", "7"}, {"60", "<UD>", "8"}, {"60", "<ASIG>", "9"}, {"60", "<WHILE>", "10"}, {"60", "<IF>", "11"}, {"60", "<Tipo>", "12"},};
    static String[] productions = {
            /*0)*/"<P’> | <P>$",
            /*1)*/"<P> | lemonade ID { <BS> }",
            /*2)*/"<BS> | <US> <BS>",
            /*3)*/"<BS> | <US>",
            /*4)*/"<US> | <UD>",
            /*5)*/"<US> | <ASIG>",
            /*6)*/"<US> | <WHILE>",
            /*7)*/"<US> | <IF>",
            /*8)*/"<UD> | <Tipo> <ASIG>",
            /*9)*/"<Tipo> | number",
            /*10*/"<ASIG> | ID = <E> ;",
            /*11*/"<WHILE> | while ( <COND> ) { <BS> }",
            /*12*/"<IF> | if ( <COND> ) { <BS> }",
            /*13*/"<IF> | if ( <COND> ) { <BS> } else { <BS> }",
            /*14*/"<COND> | ID <OP_REL> ID",
            /*15*/"<OP_REL> | <",
            /*16*/"<OP_REL> | >",
            /*17*/"<OP_REL> | ==",
            /*18*/"<OP_REL> | <=",
            /*19*/"<OP_REL> | >=",
            /*20*/"<OP_REL> | !=",
            /*21*/"<E> | <E> + <T>",
            /*22*/"<E> | <E> - <T>",
            /*23*/"<E> | <T>",
            /*24*/"<T> | <T> * <F>",
            /*25*/"<T> | <T> / <F>",
            /*26*/"<T> | <F>",
            /*27*/"<F> | ( <E> )",
            /*28*/"<F> | ID",
            /*29*/"<F> | NUM",
    };
}
