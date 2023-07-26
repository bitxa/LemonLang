package org.example.analizers;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.example.symbols.Token;
import org.example.symbols.TokenPattern;

public class TokenTypeGetter {
    private ArrayList<String> tokens;
    private ArrayList<Token> verifiedTokens = new ArrayList<>();
    private ArrayList<String> onlyTypes = new ArrayList<>();

    public TokenTypeGetter(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    public void getTypes() {
        for (String token : tokens) {
            if (Pattern.matches(TokenPattern.NUM_PATTERN, token)) {
                verifiedTokens.add(new Token(token, "NUM"));
            } else if (Pattern.matches(TokenPattern.PALABRA_RESERVADA_PATTERN, token)) {
                verifiedTokens.add(new Token(token, token));
            } else if (Pattern.matches(TokenPattern.IDENTIFIER_PATTERN, token)) {
                verifiedTokens.add(new Token(token, "ID"));
            } else {
                verifiedTokens.add(new Token(token, token));
            }
        }
    }

    public ArrayList<Token> getVerifiedTokens() {
        return verifiedTokens;
    }

    public ArrayList<String> getOnlyTypes() {
        for (Token token : verifiedTokens) {
            onlyTypes.add(token.type);
        }
        return onlyTypes;
    }

}
