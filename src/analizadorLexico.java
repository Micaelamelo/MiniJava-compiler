import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class analizadorLexico {
	
	private FileReader entrada;
	private BufferedReader buffer; 
	private FileWriter archivoEspacio;
	
	private List<String> palabrasReservadas;	
	private Map<String,String> Tokens;
	
	private boolean finArchivo;
	
	private int nroLinea;
	private int nroColumna;
	private int estado;
	private int caracterEntero;
	
	private String lexemaActual;
	
	private char caracterActual;

	/**
	 * Constructor
	 * @param archivo: nombre del archivo a leer.
	 */
	public analizadorLexico(String archivo){
		
		try {
			
			entrada = new FileReader(archivo);
			
			buffer= new BufferedReader(entrada);
			
			try {
				archivoEspacio= new FileWriter(archivo,true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//voy a necesitar de un espacio en blanco al final de mi archivo
	
		try {
			archivoEspacio.write(" ");
			archivoEspacio.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//inicializo variables 
		
		nroLinea = 1;
		nroColumna = 0;
		
		finArchivo = false;

		palabrasReservadas= new ArrayList<String>();
		Tokens= new HashMap<String, String>();
		
		//agrego palabras reservadas a una lista
		palabrasReservadas.add("class");
		palabrasReservadas.add("extends");
		palabrasReservadas.add("static");
		palabrasReservadas.add("dynamic");
		palabrasReservadas.add("void");
		palabrasReservadas.add("boolean");
		palabrasReservadas.add("char");  
		palabrasReservadas.add("int");
		palabrasReservadas.add("String");
		palabrasReservadas.add("public");
		palabrasReservadas.add("private");
		palabrasReservadas.add("if");
		palabrasReservadas.add("else");
		palabrasReservadas.add("while");
		palabrasReservadas.add("return");
		palabrasReservadas.add("this");
		palabrasReservadas.add("new");
		palabrasReservadas.add("null");
		palabrasReservadas.add("true");
		palabrasReservadas.add("false");
		

		Tokens.put("{","LLave abre");
		Tokens.put("}","Llave cierra");
		Tokens.put(";","Punto y coma");
		Tokens.put(".","Punto");
		Tokens.put(",","Coma");
		Tokens.put("(","Parentesis abre");
		Tokens.put(")","Parentesis cierra");
		Tokens.put("[","Corchete abre");
		Tokens.put("]", "Corchete cierra");
		
		Tokens.put("+","Signo +");
		Tokens.put("-","Signo -");
		Tokens.put("*","Signo *");
		Tokens.put("/","Signo /");
		Tokens.put(">","Mayor");
		Tokens.put(">=", "Mayor o igual");
		Tokens.put("<", "Menor");
		Tokens.put("<=", "Menor o igual");
		Tokens.put("=", "Asignacion");
		Tokens.put("==", "Comparacion");
		Tokens.put("!", "Not");
		Tokens.put("!=", "Distinto");
		
		//comienzo a leer
		nextChar();
	}
	
	/**
	 * Metodo nextChar(): Lee el siguiente caracter en el archivo.
	 */
	private void nextChar() {
		
			try {
				caracterEntero = buffer.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (caracterEntero  != -1){ //si no llegue al fin del archivo
				caracterActual = (char) caracterEntero;
				nroColumna++;
			}
			else
				finArchivo = true;
	}
	
	/**
	 * Metodo consumir(): Agrega al lexema actual el caracter leido y lee el siguiente caracter.
	 */
	private void consumir() {
		lexemaActual = lexemaActual+ caracterActual;
		nextChar();
	}
	
	/**
	 * Metodo esMayus: Analiza caracter para determinar si es mayuscula
	 * @param c Caracter a analizar
	 * @return Devuelve verdadero si c es una letra mayuscula, falso caso contrario.
	 */
	private boolean esMayus(char c){
		return Character.isUpperCase(c);
	}
	
	/**
	 * Metodo esMinus: Analiza caracter para determinar si es minuscula
	 * @param c Caracter a analizar
	 * @return Devuelve verdadero si c es una letra minuscula, falso caso contrario.
	 */
	private boolean esMinus(char c){
		return Character.isLowerCase(c);
	}
	
	/**
	 * Metodo esDigito(): Analiza caracter para determinar si es digito
	 * @param c Caracter a analizar
	 * @return Devuelve verdadero si c es un digito, falso caso contrario.
	 */
	private boolean esDigito(char c){
		return Character.isDigit(c);
	}
	
	/**
	 * Metodo esEspacio(): Analiza caracter para determinar si es espacio
	 * @param c Caracter a analizar
	 * @return Devuelve verdadero si c es espacio en blanco, salto de linea, retorno de carro, tab; falso caso contrario.
	 */
	private boolean esEspacio(char c){
		return Character.isWhitespace(caracterActual);
	}
	
	/**
	 * Metodo setLineaColumna(): Lee el siguiente caracter en el archivo, si es un salto de linea incrementa nro de linea y resetea nro de columna.
	 */
	private void setLineaColumna(){
		nextChar();
		if (caracterActual == '\n'){
			nroLinea++;
			nroColumna=0; //reseteo el numero de columnas en cada linea
		}
	}
		
	/**
	 * Metodo getToken(): Retorna un token.
	 * @return Token con el tipo de token, lexema, nro de linea y nro de columna.
	 * @throws Exception
	 */
	public Token getToken() throws Exception{
	estado=0;
	lexemaActual="";
	
		while(!finArchivo){
			
			switch(estado){
				case 0:
					
					if(esEspacio(caracterActual)){
						estado=0;
						setLineaColumna();
					}		
					
					else 
						if (esMayus(caracterActual)){	
							estado = 1;
							consumir();
					}			
					
					else 
						if (esMinus(caracterActual)){
							estado = 2;
							consumir();
					}
					
					else 
						if (esDigito(caracterActual)){
							estado =3;
							consumir();
					}

					else 
						if (caracterActual == '/'){
							estado = 4; 
							consumir();
					}
										
					else 
						if (caracterActual == '\''){
							estado = 5; 
							nextChar();
					}
					
					else 
						if (caracterActual == '"'){
							estado = 6; 
							nextChar();
					}
					
					else
						if (caracterActual == '&'){
							estado = 7; 
							consumir();
					}
					
					else 
						if (caracterActual == '|'){
							estado = 8; 
							consumir();
					}
					
					else 
						if(caracterActual == '='){
							estado=9; 
							consumir();	
					}
					
					else 
						if (caracterActual == '>' || 
							caracterActual == '<' || 
							caracterActual == '!'){
							
							estado = 9; 
							consumir();	
					}
					
					else 
						if (caracterActual == '+' || 
							caracterActual == '-' || 
							caracterActual == '*' ){
							
							consumir();
							return new Token(Tokens.get(lexemaActual), lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					}
					 
					else 
						if (caracterActual == '{' || 
							caracterActual == '}' || 
							caracterActual == '[' || 
							caracterActual == ']' || 
							caracterActual == '(' ||  
							caracterActual == ')' || 
							caracterActual == ';' || 
							caracterActual == ',' || 
							caracterActual == '.') {
							
							consumir();
							return new Token(Tokens.get(lexemaActual), lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					}
					
					else
						throw new Exception("Caracter invalido en la linea: "+nroLinea);
					
					break;
								
				case 1:
					
					if(caracterActual == '_' 	 ||  
						esDigito(caracterActual) || 
						esMayus(caracterActual)  || 
						esMinus(caracterActual)){
						
						estado=1; 
						consumir();
					}
					else {
						if(palabrasReservadas.contains(lexemaActual))
							return new Token(lexemaActual, lexemaActual, nroLinea, nroColumna-lexemaActual.length());			
						else 
							return new Token("Id de clase", lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					}
					
					break;
				
				case 2:
					
					if(caracterActual == '_' 	 || 
						esDigito(caracterActual) || 
						esMayus(caracterActual)  || 
						esMinus(caracterActual)) {
						
						estado=2;
						consumir();
					}
					else{
						if(palabrasReservadas.contains(lexemaActual))
							return new Token(lexemaActual, lexemaActual, nroLinea, nroColumna-lexemaActual.length());			
						else
							return new Token("Id Metodo-Variable", lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					}
					
					break;
					
				case 3: 
					
					if(esDigito(caracterActual)){
						estado=3; 
						consumir();					
					}
					else
						return new Token("Entero", lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					
					break;
				
				case 4: 
					
					if (caracterActual == '/') {
						estado =10; 
						consumir();
					}
					else if (caracterActual == '*') {
						estado = 11; 
						consumir();
					}
					else 
						return new Token(Tokens.get(lexemaActual), lexemaActual, nroLinea, nroColumna-lexemaActual.length()); //si es la barra de division
					
					break;
					
				case 5:
					
					if (caracterActual == '\\') {
						estado = 13; 
						nextChar();
					}
					
					else 
						if (esEspacio(caracterActual))
							throw new Exception("Caracter invalido, comilla simple sola, en linea: "+nroLinea);
					
					else 
						if (caracterActual == '\'') 
							throw new Exception("Caracter invalido, dos comilas simples juntas, en linea: "+nroLinea);
					
					else {
						estado = 14; 
						consumir();
					}
					
					break;
				
				case 6:
					
					if (caracterActual == '"') {
						nextChar();
						return new Token("String", lexemaActual, nroLinea, nroColumna-lexemaActual.length()-2);
					}
					
					else 
						if (caracterActual == '\n')
							throw new Exception("String invalido con saltos de linea en el medio, en la linea: "+nroLinea);
					
					else {
						consumir();						
						if (finArchivo) 
							throw new Exception("String no cerrado, en la linea: "+nroLinea);
						else 
							estado=6;
					}
					
					break;
									
				case 7:
					
					if (caracterActual == '&') {
						consumir();
						return new Token("And", lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					}
					else 
						throw new Exception("Operador & invalido en la linea: "+nroLinea);
					
				case 8:
					
					if (caracterActual == '|') {
						consumir();
						return new Token("Or", lexemaActual, nroLinea, nroColumna-lexemaActual.length());
					}
					else
						throw new Exception("Operador | invalido en la linea: "+nroLinea);
					
				case 9:
					
					if (caracterActual == '=') 
						consumir();
					
					return new Token(Tokens.get(lexemaActual), lexemaActual, nroLinea, nroColumna-lexemaActual.length());

				case 10: 
					
					if(caracterActual == '\n'){
						estado=0;
						nroColumna=0;
						nroLinea++;
						consumir();
						lexemaActual="";
					}
					else{
						estado=10; 
						consumir();
					}
					
					break;		
				
				case 11:	
					
					if (!finArchivo) {
						if (caracterActual == '*') {
							estado = 12; 
							consumir();
						}
						else {
							estado = 11; 
							setLineaColumna();
							if (finArchivo)
								throw new Exception("Comentario /* */ no cerrado en la linea: "+nroLinea);
						}
					}		
					
					break;
				
				case 12: 
									
					if (caracterActual == '/') {
							estado = 0;
							consumir();
							lexemaActual="";
					}
					else 
						if (caracterActual == '*') {
							estado = 12; 
							consumir();
							if (finArchivo)
								throw new Exception("Comentario /* */ no cerrado en la linea: "+nroLinea);
						}
						else {
							estado = 11; 
							consumir();
							if (finArchivo)
								throw new Exception("Comentario /* */ no cerrado en la linea: "+nroLinea);
						}
					
					break;
					
				case 13:
					
					estado = 15; 
					consumir();
					
					if (finArchivo)
						throw new Exception("Falta cerrar comilla simple o falta caracter en la linea "+nroLinea);
		
					break;
					
				case 14: 
					
					if (caracterActual == '\'') {
						nextChar();
						return new Token("Caracter", lexemaActual, nroLinea, nroColumna-lexemaActual.length()-2);
					}
					else
						throw new Exception("Falta cerrar comilla simple, en linea "+nroLinea);

						
				case 15:
					
					if (caracterActual == '\'') {
						nextChar();
						
						if(lexemaActual.equals("n"))
							return new Token ("Caracter", "\\n", nroLinea, nroColumna-lexemaActual.length());
						else 
							if(lexemaActual.equals("t"))
								return new Token ("Caracter", "\\t", nroLinea, nroColumna-lexemaActual.length());
							else	
								return new Token("Caracter", lexemaActual, nroLinea, nroColumna-lexemaActual.length()-3);
					}
					else 
						throw new Exception("Falta cerrar comilla simple o falta caracter, en linea  "+nroLinea);
					
			}
				
			
			if(finArchivo)
				return new Token("EOF", "End Of File", nroLinea, nroColumna-lexemaActual.length());
		}
		
	return null;
		
		
	}

}



