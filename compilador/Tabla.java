package ehhCabeza;

import java.util.ArrayList;

public class Tabla {

	final int clase = 0, publico = 1, privado = 2, whilex = 3, entero = 4, booleano = 5, llaveizq = 6,
			llaveder = 7, EQ = 8, semi = 9,menor = 10, mayor = 11, d2EQ = 12, menorEQ = 13, mayorEQ = 14, diferente =15,
			difEQ = 16,	truex = 17, falsex = 18, brackizq = 19, brackder = 20, div = 21, mas = 22, 
			menos = 23, mult = 24, ifx = 25, num = 50,   ID = 52; //bool = 21,
	
	ArrayList<Token> tokenRC;
	ArrayList<ValoresTabla> valoresTab = new ArrayList<ValoresTabla>();
	
	public Tabla(ArrayList<Token> tokenrc) 
	{

//		public String rango, tipo, nombre, valor, renglon, columna;
		
		tokenRC = tokenrc;
		String nombre[] = new String[tokenRC.size()];
		int tipo[] = new int[tokenRC.size()];
		String nombreTipo = "";
		String renglon[] = new String[tokenRC.size()];
		String columna[] = new String[tokenRC.size()];
		
		//desmonto valores del arraylist en arreglos para su uso más fácil
		
		for (int i = 0; i < tokenrc.size(); i++) 
		{
			nombre[i] = tokenrc.get(i).getToken();
			tipo[i] = tokenrc.get(i).getTipo();
			renglon[i] = String.valueOf(tokenrc.get(i).getRenglon());
			columna[i] = String.valueOf(tokenrc.get(i).getColumna());

		}
		
		//Asigna valores al arraylist que desplegará la tabla
		for (int i = 0; i < tokenrc.size(); i++) {
			
			if(tipo[i] == entero || tipo[i] == booleano) {
				
				if(tipo[i] == entero)
					nombreTipo = "int";
				else
					nombreTipo = "boolean";
				
				if(tipo[i-1] == publico)
				{
					if(nombreTipo.equals("int"))
						ValoresHaciaTabla("public", nombreTipo, nombre[i+1],     "0", renglon[i+1], columna[i+1]);
					else
						ValoresHaciaTabla("public", nombreTipo, nombre[i+1], "false", renglon[i+1], columna[i+1]);
						
				}
				else if(tipo[i-1] == privado)
				{
					if(nombreTipo.equals("int"))
						ValoresHaciaTabla("private", nombreTipo, nombre[i+1],     "0", renglon[i+1], columna[i+1]);
					else
						ValoresHaciaTabla("private", nombreTipo, nombre[i+1], "false", renglon[i+1], columna[i+1]);
				}
				else
				{
					if(nombreTipo.equals("int"))
						ValoresHaciaTabla("S/M", nombreTipo, nombre[i+1],     "0", renglon[i+1], columna[i+1]);
					else
						ValoresHaciaTabla("S/M", nombreTipo, nombre[i+1], "false", renglon[i+1], columna[i+1]);
				}
				
			}
				
		}
		//Aquí a las variables declaradas se les asignan lo valores correspondientes al código en el .txt
		for (int i = 0; i < tokenRC.size(); i++) {
			
			for (int j = 0; j < valoresTab.size(); j++) 
			{
				if(tokenRC.get(i).getToken().equals(valoresTab.get(j).nombre))
					if(tipo[i] == ID && tipo[i+1] == EQ)
					{
						
						if(tipo[i+3] == mas || tipo[i+3] == menos || tipo[i+3] == div || tipo[i+3] == mult)
						{
							valoresTab.set(
									j,
									new ValoresTabla(
												valoresTab.get(j).rango,
												valoresTab.get(j).tipo,
												valoresTab.get(j).nombre,
												tokenRC.get(i+2).getToken() + " " + tokenRC.get(i+3).getToken() + " " + tokenRC.get(i+4).getToken(),
												valoresTab.get(j).renglon,
												valoresTab.get(j).columna					
											)							
									);
						}
						else
						{
							
							valoresTab.set(
									j,
									new ValoresTabla(
												valoresTab.get(j).rango,
												valoresTab.get(j).tipo,
												valoresTab.get(j).nombre,
												tokenRC.get(i+2).getToken(),
												valoresTab.get(j).renglon,
												valoresTab.get(j).columna					
											)							
									);
							
						}
					}
			}
			
		}
		//Imprime la tabla de simbolos con sus datos
		System.out.println("\n"+
				"No."		+ blancos("no.       ")		+
				"Modificador" 	+ blancos("modificador") 		+
				"Tipo" 		+ blancos("tipo")		+
				"Nombre"	+ blancos("nombre") 	+
				"Valor"		+ blancos("valor") 		+
				"Renglon" 	+ blancos("renglon") 	+
				"Columna o No. de token" 	+ blancos("columna o No. de token") 	+
				"\n"
				);
		for (int i = 0; i < valoresTab.size(); i++)
		{
			System.out.println(
							(i+1) 						+ blancos(String.valueOf((i+1+"       ")))		+
							valoresTab.get(i).rango 	+ blancos(valoresTab.get(i).rango) 		+
							valoresTab.get(i).tipo 		+ blancos(valoresTab.get(i).tipo) 		+
							valoresTab.get(i).nombre	+ blancos(valoresTab.get(i).nombre) 	+
							valoresTab.get(i).valor 	+ blancos(valoresTab.get(i).valor) 		+
							valoresTab.get(i).renglon 	+ blancos(valoresTab.get(i).renglon) 	+
							valoresTab.get(i).columna 	+ blancos(valoresTab.get(i).columna) 
					
					);
		}
	}

	public void ValoresHaciaTabla(String ran, String tip, String nom, String val, String reng, String col) 
	{
		valoresTab.add(new ValoresTabla(ran, tip, nom, val, reng, col));
	}
	
	public String blancos(String cadena) {
		
		String blancos = "";
		
		for (int i = cadena.length(); i < 15; i++) {
			blancos += " ";
		}
		
		return blancos;
	}
}
