package org.example.analizers;

import java.util.Stack;

class Parser {
    private String entrada;
    private int posicion;

    public Parser(String entrada) {
        this.entrada = entrada;
        this.posicion = 0;
    }

    public String siguienteSimbolo() {
        if (posicion < entrada.length()) {
            if (entrada.startsWith("id", posicion)) {
                posicion += 2; // Jump over 'id'
                return "id";
            } else {
                return String.valueOf(entrada.charAt(posicion++));
            }
        } else {
            return "$";
        }
    }
}


public class Analizador {
    private String[][] matrizValores = {
            {"0", "id", "d5"}, {"0", "+", "error"}, {"0", "*", "error"}, {"0", "(", "d4"}, {"0", ")", "error"}, {"0", "$", "error"},
            {"1", "id", "error"}, {"1", "+", "d6"}, {"1", "*", "error"}, {"1", "(", "error"}, {"1", ")", "error"}, {"1", "$", "acep"},
            {"2", "id", "error"}, {"2", "+", "r2"}, {"2", "*", "d7"}, {"2", "(", "error"}, {"2", ")", "r2"}, {"2", "$", "r2"},
            {"3", "id", "error"}, {"3", "+", "r4"}, {"3", "*", "r4"}, {"3", "(", "error"}, {"3", ")", "r4"}, {"3", "$", "r4"},
            {"4", "id", "d5"}, {"4", "+", "error"}, {"4", "*", "error"}, {"4", "(", "d4"}, {"4", ")", "error"}, {"4", "$", "error"},
            {"5", "id", "error"}, {"5", "+", "r6"}, {"5", "*", "r6"}, {"5", "(", "error"}, {"5", ")", "r6"}, {"5", "$", "r6"},
            {"6", "id", "d5"}, {"6", "+", "error"}, {"6", "*", "error"}, {"6", "(", "d4"}, {"6", ")", "error"}, {"6", "$", "error"},
            {"7", "id", "d5"}, {"7", "+", "error"}, {"7", "*", "error"}, {"7", "(", "d4"}, {"7", ")", "error"}, {"7", "$", "error"},
            {"8", "id", "error"}, {"8", "+", "d6"}, {"8", "*", "error"}, {"8", "(", "error"}, {"8", ")", "d11"}, {"8", "$", "error"},
            {"9", "id", "error"}, {"9", "+", "r1"}, {"9", "*", "d7"}, {"9", "(", "error"}, {"9", ")", "r1"}, {"9", "$", "r1"},
            {"10", "id", "error"}, {"10", "+", "r3"}, {"10", "*", "r3"}, {"10", "(", "error"}, {"10", ")", "r3"}, {"10", "$", "r3"},
            {"11", "id", "error"}, {"11", "+", "r5"}, {"11", "*", "r5"}, {"11", "(", "error"}, {"11", ")", "r5"}, {"11", "$", "r5"}
    };

    public String buscarEnMatriz(int estado, String simbolo) {
        for (int i = 0; i < matrizValores.length; i++) {
            if (Integer.parseInt(matrizValores[i][0]) == estado && matrizValores[i][1].equals(simbolo)) {
                return matrizValores[i][2];
            }
        }
        return "error";
    }

    public int accion(int estado, String simbolo) {
        String accion = buscarEnMatriz(estado, simbolo);

        if (accion.equals("error")) {
            System.out.println("No se encontró un match en la matriz. Estado: " + estado + " Simbolo: " + simbolo);
            return -1;
        } else if (accion.equals("acep")) {
            return 0;
        } else {
            int nuevoEstado = Integer.parseInt(accion.substring(1));
            if (accion.charAt(0) == 'd') {
                return nuevoEstado;
            } else if (accion.charAt(0) == 'r') {
                return -nuevoEstado;
            } else {
                System.out.println("No se reconoce la acción: " + accion);
                return -1;
            }
        }
    }

    public void ejecutar(Parser parser) {
        int estado = 0;
        String simbolo;
        Stack<Integer> pila = new Stack<>();
        pila.push(estado);

        while (!pila.empty() && !"$".equals(simboloActual)) {
            int estado = pila.peek();
            int siguienteEstado = matrizLR.get(estado).get(simboloActual);

            if (siguienteEstado < 0) {
                // This is a reduction operation
                Gramatica g = gramaticas.get(-siguienteEstado - 1);

                // Ensure we have enough items to pop
                if (pila.size() < g.getLadoDerecho().size()) {
                    System.err.println("Error: not enough items on the stack to perform reduction.");
                    return;
                }

                for (int i = 0; i < g.getLadoDerecho().size(); i++) {
                    pila.pop();
                }

                if (!pila.empty()) {
                    estado = pila.peek();
                } else {
                    System.err.println("Error: stack is empty after reduction.");
                    return;
                }
                simboloActual = g.getLadoIzquierdo();
                pila.push(matrizLR.get(estado).get(simboloActual));
            } else if (siguienteEstado == 0) {
                System.err.println("Error: no transition available.");
                return;
            } else {
                pila.push(siguienteEstado);
                simboloActual = parser.siguienteSimbolo();
            }
        }
    }

    public static void main(String[] args) {
        Parser parser = new Parser("id+id");
        Analizador analizador = new Analizador();
        analizador.ejecutar(parser);
    }
}




