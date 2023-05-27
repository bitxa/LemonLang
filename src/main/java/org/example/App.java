package org.example;

import org.example.analizers.LexicalAnalizer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws Exception {
        LexicalAnalizer analizer = new LexicalAnalizer();
        analizer.collectTokens();

    }
}
