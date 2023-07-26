package org.example.analizers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Definición de las posibles acciones que pueden ocurrir durante el análisis LR
enum TipoDeAccion {
    DESPLAZAR,
    REDUCIR,
    ACEPTAR,
}

// Clase principal para el análisis sintáctico LR(0)
public class AnalizadorSintactico {
    // Elementos que necesitaremos para el análisis LR
    private final List<String> tokensDeEntrada;
    private final Stack<Integer> pilaDeEstados;
    private final Stack<String> pilaDeSimbolos;
    private final Map<Integer, Map<String, AccionLR>> tablaDeAcciones;
    private final Map<Integer, Map<String, Integer>> tablaGoto;
    private final Gramatica gramatica;

    // Constructor del analizador sintáctico
    public AnalizadorSintactico(List<String> tokensDeEntrada) {
        this.tokensDeEntrada = tokensDeEntrada;
        this.pilaDeEstados = new Stack<>();
        this.pilaDeSimbolos = new Stack<>();
        this.tablaDeAcciones = new HashMap<>();
        this.tablaGoto = new HashMap<>();
        this.gramatica = new Gramatica();
    }

    // Cargar la tabla LR desde un archivo CSV
    public void cargarTablaLRDesdeCSV(String rutaArchivoCSV) {
        // Intentar abrir y leer el archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCSV))) {
            String linea;
            int estadoActual = 0;
            int columna = 0;
            List<String> simbolosTerminales = new ArrayList<>();

            // Leer cada línea del archivo CSV
            while ((linea = br.readLine()) != null) {
                String[] entradas = linea.split(";");
                if (estadoActual == 0) {
                    // Leer todas las acciones de la tabla
                    for (int i = 1; i < entradas.length; i++) {
                        if (entradas[i].equals("$")) {
                            columna = i + 1;
                        } else {
                            simbolosTerminales.add(entradas[i]);
                            Map<String, AccionLR> acciones = new HashMap<>();
                            acciones.put(entradas[i], crearAccion(entradas[i], simbolosTerminales.get(simbolosTerminales.size() - 1)));
                            tablaDeAcciones.put(estadoActual, acciones);

                            // Verificar si la entrada es un símbolo terminal y agregarlo a la Gramática
                            if (!entradas[i].startsWith("d")) {
                                gramatica.agregarSimboloTerminal(entradas[i]);
                            }
                        }
                    }
                } else {
                    // Leer todos los "goto" de la tabla
                    int estado = Integer.parseInt(entradas[0]);
                    Map<String, Integer> mapaGoto = new HashMap<>();

                    for (int i = columna; i < entradas.length; i++) {
                        if (entradas[i].startsWith("d")) {
                            int siguienteEstado = Integer.parseInt(entradas[i].substring(1));
                            mapaGoto.put(simbolosTerminales.get(i - columna), siguienteEstado);
                        } else if (!entradas[i].isEmpty()) {
                            tablaDeAcciones.computeIfAbsent(estado, k -> new HashMap<>())
                                    .put(simbolosTerminales.get(i - columna), crearAccion(entradas[i], simbolosTerminales.get(i - columna)));

                            // Verificar si la entrada es un símbolo terminal y agregarlo a la Gramática
                            if (!entradas[i].startsWith("r") && !entradas[i].startsWith("d")) {
                                gramatica.agregarSimboloTerminal(entradas[i]);
                            }
                        }
                    }
                    tablaGoto.put(estado, mapaGoto);
                }
                estadoActual++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para imprimir la tabla de acciones para la depuración
    void imprimirTablaDeAcciones() {
        int maximoEstado = Collections.max(tablaDeAcciones.keySet());
        for (int estado = 0; estado <= maximoEstado; estado++) {
            if (tablaDeAcciones.containsKey(estado)) {
                Map<String, AccionLR> acciones = tablaDeAcciones.get(estado);

                System.out.println("Estado " + estado + " Acciones:");
                for (Map.Entry<String, AccionLR> entradaAccion : acciones.entrySet()) {
                    String simboloTerminal = entradaAccion.getKey();
                    AccionLR accion = entradaAccion.getValue();

                    System.out.println("  Símbolo Terminal: " + simboloTerminal);
                    System.out.println("  Tipo de Acción: " + accion.getTipo());
                    System.out.println("  Siguiente Estado: " + accion.getSiguienteEstado());
                    System.out.println("  Regla: " + accion.getRegla());
                    System.out.println();
                }
            } else {
                System.out.println("Estado " + estado + " no tiene acciones definidas.");
                System.out.println();
            }
        }
    }

    // Crear una acción basada en la cadena de entrada
    private AccionLR crearAccion(String accionStr, String simbolo) {
        if (accionStr.equals("accep")) {
            return new AccionLR(TipoDeAccion.ACEPTAR, -1, null);
        } else if (accionStr.startsWith("d")) {
            int siguienteEstado = Integer.parseInt(accionStr.substring(1));
            return  new AccionLR(TipoDeAccion.DESPLAZAR, siguienteEstado, simbolo); // Usamos el nuevo valor 'SHIFT'
        } else {
            return new AccionLR(TipoDeAccion.REDUCIR, -1, accionStr);
        }
    }

    // Desplazar un token al stack
    private void desplazar(int siguienteEstado, List<String> copiaDeTokens) {
        pilaDeEstados.push(siguienteEstado);
        pilaDeSimbolos.push(copiaDeTokens.get(0));
        copiaDeTokens.remove(0);
        imprimirPilas();
    }

    // Reducir el stack basándose en una regla de gramática
    private void reducir(String regla) {
        List<String> produccion = gramatica.getProduccion(regla);
        for (int i = produccion.size() - 1; i >= 0; i--) {
            pilaDeEstados.pop();
            pilaDeSimbolos.pop();
        }
        String simboloNoTerminal = produccion.get(0);
        pilaDeSimbolos.push(simboloNoTerminal);
        int estadoActual = pilaDeEstados.peek();
        int siguienteEstado = tablaGoto.get(estadoActual).get(simboloNoTerminal);
        pilaDeEstados.push(siguienteEstado);
        imprimirPilas();
    }

    // Método principal para el análisis sintáctico
    public void analizar() {
        pilaDeEstados.push(0);
        List<String> copiaDeTokens = new ArrayList<>(tokensDeEntrada);
        copiaDeTokens.add("$");

        while (true) {
            int estadoActual = pilaDeEstados.peek();
            String simboloActual = copiaDeTokens.get(0);

            if (!tablaDeAcciones.containsKey(estadoActual)) {
                throw new RuntimeException("Error de análisis: no hay acciones definidas para el estado " + estadoActual);
            }
            Map<String, AccionLR> acciones = tablaDeAcciones.get(estadoActual);

            if (!acciones.containsKey(simboloActual)) {
                throw new RuntimeException("Error de análisis: no hay acciones definidas para el símbolo " + simboloActual);
            }
            AccionLR accion = acciones.get(simboloActual);

            if (accion.getTipo() == TipoDeAccion.DESPLAZAR) {
                desplazar(accion.getSiguienteEstado(), copiaDeTokens);
            } else if (accion.getTipo() == TipoDeAccion.REDUCIR) {
                reducir(accion.getRegla());
            } else if (accion.getTipo() == TipoDeAccion.ACEPTAR) {
                System.out.println("La entrada ha sido aceptada.");
                break;
            }
        }
    }

    // Método para imprimir el estado actual de las pilas para la depuración
    private void imprimirPilas() {
        System.out.println("Pila de estados: " + pilaDeEstados);
        System.out.println("Pila de símbolos: " + pilaDeSimbolos);
        System.out.println();
    }
}

// Clase para representar una acción de análisis LR
class AccionLR {
    private final TipoDeAccion tipo;
    private final int siguienteEstado;
    private final String regla;

    public AccionLR(TipoDeAccion tipo, int siguienteEstado, String regla) {
        this.tipo = tipo;
        this.siguienteEstado = siguienteEstado;
        this.regla = regla;
    }

    public TipoDeAccion getTipo() {
        return tipo;
    }

    public int getSiguienteEstado() {
        return siguienteEstado;
    }

    public String getRegla() {
        return regla;
    }
}

// Clase para almacenar y administrar la gramática del análisis
class Gramatica {
    private final Map<String, List<String>> reglas;
    private final Set<String> simbolosTerminales;
    private final Set<String> simbolosNoTerminales;

    // Constructor de la clase Gramatica
    public Gramatica() {
        reglas = new HashMap<>();
        simbolosTerminales = new HashSet<>();
        simbolosNoTerminales = new HashSet<>();
    }

    // Método para agregar una nueva regla de gramática
    public void agregarRegla(String simboloNoTerminal, List<String> produccion) {
        reglas.put(simboloNoTerminal, produccion);
    }

    // Método para obtener una producción de gramática
    public List<String> getProduccion(String simboloNoTerminal) {
        return reglas.get(simboloNoTerminal);
    }

    // Método para agregar un símbolo terminal a la gramática
    public void agregarSimboloTerminal(String simbolo) {
        simbolosTerminales.add(simbolo);
    }

    // Método para agregar un símbolo no terminal a la gramática
    public void agregarSimboloNoTerminal(String simbolo) {
        simbolosNoTerminales.add(simbolo);
    }

    // Método para verificar si un símbolo es terminal
    public boolean esSimboloTerminal(String simbolo) {
        return simbolosTerminales.contains(simbolo);
    }

    // Método para verificar si un símbolo es no terminal
    public boolean esSimboloNoTerminal(String simbolo) {
        return simbolosNoTerminales.contains(simbolo);
    }
}
