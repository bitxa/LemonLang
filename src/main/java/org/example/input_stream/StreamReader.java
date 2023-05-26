package org.example.input_stream;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class StreamReader {
        private Reader reader;
        private BufferedReader buffer;

        public StreamReader(String fileName) throws FileNotFoundException {
            this.reader = new FileReader(fileName);
            this.buffer = new BufferedReader(this.reader);
        }


        public BufferedReader buffer(){
            return buffer;
        }

        Reader reader(){
            return reader;
        }

    }


