# Lemon Lang 

Repositorio: [https://github.com/bitxa/LemonLang](https://github.com/bitxa/LemonLang)

## Autores

* [Joel Romero](https://github.com/bitxa)
* [Fabián Montoya](https://github.com/f4biaan)

## Comandos y Sintaxis

* ***Crear un nuevo programa***

  Para crear un programa en el Lenguaje de Programación ***Lemon*** se debe utilizar la siguiente estructura que comienza con la palabra reservada *lemonade* seguido del nombre del programa y el codigo continido dentro de llaves ( ***{}*** ):

  ```
  lemonade myProgram {
    <code>
  }
  ```

  Además de estos delimitadors de inicio y de fin de programa, también existen delimitadores para cada línea de código, este será el  ';'.

  ```
  lemonade myProgram {
    number var1 = 1;
  }
  ```
* ***Comentarios***
  ```
  $ This is a comment
  ```
* ***Declaracion de variables***

  Una declaración o instrucción se utiliza para introducir una valor, un identiﬁcador, o una acción, en el programa o módulo. Cada declaración termina con el carácter " ; ". En LemonLang para declarar un valor se usa el formato:

  ```
    <tipo> <identiﬁcador> = <contenido>;
  ```

  * ***Tipos de variables:***
    * Tipo numérico
    ```
    number myNum = 200;
    number newNum = 200.2;
    ```
  * **Operaciones**
    * **Aritmeticas**
    ```
    1 + 2 ;
    1 - 2 ;
    1 * 2 ;
    1 / 2 ;
    ```
    
    * **Relacionales**
    ```
    1 < 2 $retorna true;
    1 <= 1 ; $ retorna true;
    1 > 2 ; $ retorna false;
    1 >= 2 ; $ retorna false;
    ```
    * También podemos comprobar igualdad y desigualdad:

    ```
      1 == 2 ; $ falso
      1 != 2 ; $ verdadero
    ```
    
  * **Flujos de control**
    * **Condicionales**
    ```
    if (1 == 1) {
      number a = 2;
      number a = 2;
    }
    ```
    * **Ciclos repetitivos**

         Las estructuras repetitivas para ejecutar instrucciones un número finito de veces. Aqui encontramos el ciclo **for** que ejecuta las instrucciones que contiene un número finito de veces, y se debe estructurar de la siguiente manera:

    ```
    var a = 100;
    var counter = 0;
    
    while(counter <= 100){
      counter++;
    }
    ```

## Terminales

* Palabras reservadas:
  * lemonade: utilizado para definir un nuevo programa.
  * if, else: condicionales
  * while: bucle
* Tipos de datos
  * number: define un valor de tipo numérico.
* Operadores:
  * Aritmeticos:
    * Suma: +, tambien se puede utilizar para concatenar cadenas
    * Resta: -
    * Multiplicación: *
    * División: /
  * Relacionales:
    * Menor que: <
    * Mayor que: >
    * Menor igual que: <=
    * Mayor igul que: >=
    * Igual a: ==
    * Diferente a: !=

* Comentarios: para ignorar una linea la cual se corresponde a un comentario en el código se indicara con el signo dolar: '$'.

## Autómata integrado

![Automata Integrado](./imgs/automata-integrado.png "Automata Integrado")

## Separadores

| Tipo de datos  | separadores                                                                                                                                                                                                                                                                                                             |
|:--------------:|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Número decinal | Para identificar los números decimales el separador va a ser un . (punto)                                                                                                                                                                                                                                               |
|     número     | Ahora para comprobar que se haya terminado en un número o identificador, se utilizan los demás símbolos como operadores aritmeticos (+,-,*,/,%), operadores rrelacionales (<,>,=), y en caso de ser otros simbolos se comprobaran si existen en el lenguaje se continuará el análisis o en caso contrario, se continua. |
