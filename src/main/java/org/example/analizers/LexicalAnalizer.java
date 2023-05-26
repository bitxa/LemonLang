package org.example.analizers;

import org.example.input_stream.StreamReader;
import org.example.symbols.TokenPattern;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.String.valueOf;

public class LexicalAnalizer {
    StreamReader reader;
    TokenPattern pattern;

    ArrayList<String> tokens;
    public LexicalAnalizer() throws FileNotFoundException {
        this.reader = new StreamReader("assets/code.txt");
        this.pattern = new TokenPattern();
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

            for (char lexeme : chars) {
                String stringValue = valueOf(lexeme);

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
                token = new StringBuilder();
            }
        }

        tokens.removeIf(str -> str.trim().isEmpty());
    }

    public void analizeTokens(){
        for (String token : tokens){
            System.out.println("TOKEN: " + token);

        }

    }

}
