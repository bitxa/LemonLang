package org.example.symbols;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenPattern {

    public static final String NUM_PATTERN = "^-?(0|[1-9]\\d*)(\\.\\d+)?$";
    public static final String PALABRA_RESERVADA_PATTERN = "^(lemonade|if|else|while|number)$";
    public static final String IDENTIFIER_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*$";
}
