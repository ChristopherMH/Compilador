import java.util.ArrayList;

public class Sintactico<T> {
    ArrayList<Token> tokenRC;

    ArrayList<String> resultado = new ArrayList<>();
    String tok = "", esperado = "";
    int type, contando = 0, flag = 0;
    String estructura = "";

    final String cadenas[] = {"class", "public", "private", "while", "int", "boolean", "char", "float", "{", "}", "=", ";", "<", ">",   //12... Aunque no se usa como tal el "!" solo, sirve para que no lance error
            "==", "<=", ">=", "!", "!=", "true", "false", "(", ")", "/", "+", "-", "*", "if"};

    final int clase = 0, publico = 1, privado = 2, whilex = 3, entero = 4, booleano = 5, caracter = 6, real = 7, llaveizq = 8,
            llaveder = 9, EQ = 10, semi = 11, menor = 12, mayor = 13, d2EQ = 14, menorEQ = 15, mayorEQ = 16, diferente = 17,
            difEQ = 18, truex = 19, falsex = 20, brackizq = 21, brackder = 22, div = 23, mas = 24,
            menos = 25, mult = 26, ifx = 27, letra = 49, num = 50, numReal = 51, ID = 52; //letra hace referencia a un caracter ('A')

    //	public Sintactico(ArrayList<String> token, ArrayList<Integer> tipo)
    public Sintactico(ArrayList<Token> tokenRC) {
        this.tokenRC = tokenRC;
//		this.token = token;
//		this.tipo = tipo; 
        try {

            this.tok = this.tokenRC.get(0).getToken();
            this.type = this.tokenRC.get(0).getTipo();
//			this.type = this..get(0);
//			this.tok = this.token.get(0);
        } catch (Exception e) {
            System.out.println("El archivo está vacío");
            System.exit(0);
        }
        Programa();
    }

    public void Advance() {
        type = tokenRC.get(contando).getTipo();
        tok = tokenRC.get(contando).getToken();
    }

    public void eat(int esperado) {
        if (type == esperado) {
            if (++contando < tokenRC.size()) {
                Advance();
            }
        } else {
            error(esperado);
        }
    }

    public void Programa() {
        if (type == publico || type == privado)
            eat(type);
        eat(clase);
        eat(ID);

        eat(llaveizq);

        while (type == publico || type == privado) {
            eat(type);
            Declaracion();
        }
        while (type == entero || type == booleano || type == real || type == caracter)
            Declaracion();
        if (this.type == whilex || this.type == ifx || this.type == entero || this.type == booleano || type == real || type == caracter)
            Statuto();
        eat(llaveder);

//		System.out.println((tokenRC.size()) + " contador = " + contando);
        if (contando < tokenRC.size())
            error(1);
        estructura = "estructura correcta";
//		System.out.println(estructura);

    }

    public void Declaracion() {
        String tok;
        switch (type) {
            case entero:
                eat(entero);
                tok = this.tok;
                eat(ID);
                if (type == EQ) {
                    eat(EQ);
                    if (type == truex || type == falsex) {
                        errorSemantico(entero, type);
                        eat(type == truex ? truex : falsex);
                    } else if (type == letra) {
                        errorSemantico(entero, type);
                        eat(letra);
                    } else if (type == numReal) {
                        errorSemantico(entero, type);
                        eat(numReal);
                    } else
                        eat(num);
                }
                eat(semi);
                break;
            case booleano:
                eat(booleano); //boolean
                tok = this.tok;
                eat(ID); //ide
                if (type == EQ) {
                    eat(EQ); //=
                    if (type == letra) {
                        errorSemantico(booleano, type);
                        eat(letra);
                    } else if (type == numReal) {
                        errorSemantico(booleano, type);
                        eat(numReal);
                    } else if (type == num) {
                        errorSemantico(booleano, type);
                        eat(num);
                    } else
                        eat(type == truex ? truex : falsex);
                }
                eat(semi);
                break;
            case real:
                eat(real);
                tok = this.tok;
                eat(ID);
                if (type == EQ) {
                    eat(EQ);

                    if (type == letra) {
                        errorSemantico(real, type);
                        eat(letra);
                    } else if (type == num) {
                        errorSemantico(real, type);
                        eat(num);
                    } else if (type == truex || type == falsex) {
                        errorSemantico(real, type);
                        eat(type == truex ? truex : falsex);
                    } else
                        eat(numReal);
                }
                eat(semi);
                break;
            case caracter:
                eat(caracter);
                tok = this.tok;
                eat(ID);
                if (type == EQ) {
                    eat(EQ);

                    if (type == num) {
                        errorSemantico(caracter, type);
                        eat(num);
                    } else if (type == truex || type == falsex) {
                        errorSemantico(caracter, type);
                        eat(type == truex ? truex : falsex);
                    } else if (type == numReal) {
                        errorSemantico(caracter, type);
                        eat(numReal);
                    } else
                        eat(letra);
                }
                eat(semi);
                break;
        }

    }

    public void VarDeclarator() {
        eat(EQ);

        if (type == num)
            eat(num);

        if (type == falsex)
            eat(falsex);

        if (type == truex)
            eat(truex);
    }

    public void Statuto() {
        switch (type) {
            case ifx:
                eat(ifx);
                eat(brackizq);

                TestingExp();
                eat(brackder);

                eat(llaveizq);

                while (type == whilex || type == ifx || type == ID || type == booleano || type == entero || type == real || type == caracter)
                    Statuto(); //para llamar otro statement dentro del statement
                eat(llaveder);

                break;

            case whilex:
                eat(whilex);
                eat(brackizq);

                TestingExp();
                eat(brackder);
                eat(llaveizq);
                while (type == whilex || type == ifx || type == booleano || type == entero || type == real || type == caracter || type == publico || type == privado)
                    Statuto(); //para llamar otro statement dentro del statement
                eat(llaveder);
                break;
            case ID:
                eat(ID);
                eat(EQ);

                ArithmeticExp();
                eat(semi);
                while (type == whilex || type == ifx || type == booleano || type == entero || type == real || type == caracter)
                    Statuto(); //para llamar otro statement dentro del statement
                break;
            case booleano:
            case entero:
            case real:
            case caracter:
                Declaracion();
                break;
            case publico:
                eat(publico);
                Declaracion();
                break;
            case privado:
                eat(privado);
                Declaracion();
                break;
            default:
                error();
        }
    }

    public void TestingExp() {

        switch (type) {
            case ID:
                if (type == ID) {
                    eat(ID);
                } else if (type == num)
                    eat(num);

                if (LogicSimbols())
                    if (type == ID) {
                        eat(ID);
                    } else if (type == num)
                        eat(num);
                break;
            default:
                error();
                break;
        }
    }

    public void ArithmeticExp() {

        switch (type) {
            case num:

                eat(num);

            {
                if (OperandoSimbols())
                    eat(num);
            }

            break;
            default:
                error();
                break;
        }
    }

    public void error(int type) {
        try {
            String tipo = ValoresInversos(type);
            if (type == 0)
                resultado.add("\nError sintáctico, se esperaba una expresión **class** al comienzo");
            else if (type == 1)
                resultado.add("\nError sintáctico en los límites, se encontró al menos un token después de la última llave cerrada, token ** " + tok + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** " + tokenRC.get(contando).getColumna() + " **");
            else if (type == 2)
                resultado.add("\nError sintáctico en asignación, se esperaba un operador y operando antes de ** " + tok + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** " + tokenRC.get(contando).getColumna() + " **");
            else if (type == 3)
                resultado.add("\nError sintáctico en validación, se esperaba un operador lógico en lugar de ** " + tok + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** " + tokenRC.get(contando).getColumna() + " **");
            else
                resultado.add("\nError sintáctico en token ** " + tok + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** " + tokenRC.get(contando).getColumna() + " ** se esperaba un token ** " + tipo + " **");

            //System.out.println(tipo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void errorSemantico(int tipoEsperado, int tipoEncontrado) {
        try {
            if (tipoEncontrado == 49)
                resultado.add("\nError semántico de asignación en la linea " + tokenRC.get(contando).getRenglon() + ", se esperaba un dato de tipo **" + cadenas[tipoEsperado] + "**, se está intentando asignar uno de tipo **char**");
            else if (tipoEncontrado == 51)
                resultado.add("\nError semántico de asignación en la linea " + tokenRC.get(contando).getRenglon() + ", se esperaba un dato de tipo **" + cadenas[tipoEsperado] + "**, se está intentando asignar uno de tipo **float**");
            else if (tipoEncontrado == 50)
                resultado.add("\nError semántico de asignación en la linea " + tokenRC.get(contando).getRenglon() + ", se esperaba un dato de tipo **" + cadenas[tipoEsperado] + "**, se está intentando asignar uno de tipo **int**");
            else
                resultado.add("\nError semántico de asignación en la linea " + tokenRC.get(contando).getRenglon() + ", se esperaba un dato de tipo **" + cadenas[tipoEsperado] + "**, se está intentando asignar uno de tipo **" + cadenas[tipoEncontrado] + "**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void error() {
        resultado.add("Error en la sintaxis, con el siguiente token ** " + tok + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** " + tokenRC.get(contando).getColumna() + " **");
    }

    public boolean LogicSimbols() {
        if (type == menor || type == mayor || type == menorEQ || type == mayorEQ || type == d2EQ/*type == mayor || type == dobleEQ ||*/) {
            eat(type);
            return true;
        } else
            error(3);
        return false;
    }

    public boolean OperandoSimbols() {
        if (type == menos || type == mas || type == div || type == mult) {
            eat(type);
            return true;
        } else
            error(2);
        return false;
    }

    public String ValoresInversos(int type) {
        String devuelve;
        if (type == 49)
            devuelve = "caracter";
        else if (type == 50)
            devuelve = "entero";
        else if (type == 51)
            devuelve = "real";
        else if (type == 52)
            devuelve = "identificador";
        else
            devuelve = cadenas[type];

        return devuelve;
    }
}