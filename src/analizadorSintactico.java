import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class analizadorSintactico {

	private analizadorLexico AL;
	private Token tokenActual;
	
	
	public analizadorSintactico(String archivo) throws Exception{
		AL= new analizadorLexico(archivo);
		try {
			tokenActual= AL.getToken();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
		Inicial();
		
	}
	
	private void match(String nombre) throws Exception{
		
		if(nombre.equals(tokenActual.getNombre())) 
			try {
				tokenActual= AL.getToken();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		else{
			throw new Exception("Error se espera "+ nombre +" en la linea "+ tokenActual.getNroLinea() );
		}
	}
	
	//Comienzo de las reglas
	
	private void Inicial() throws Exception{
		Clases();
	}
	
	private void Clases() throws Exception{
		Clase();
		ClasesAux();
	}
	
	private void Clase() throws Exception{
		match("class");
		match("Id de clase");
		//aca agregar lo de insertarClase(idClase.lexem)
		Herencia();
		//claseactual.padre=Herencia.clase
		match("LLave abre");
		Miembros();
		match("Llave cierra");
	}
	
	private void ClasesAux() throws Exception{
		if(tokenActual.getNombre().equals("class"))
			Clases();
		else
			if (!tokenActual.getNombre().equals("EOF")) {
				throw new Exception("Se espera class o EOF en linea " +tokenActual.getNroLinea());
			}
	}
	 	
	private void Herencia() throws Exception{
		
		if(tokenActual.getNombre().equals("extends")){
			match("extends");
			match("Id de clase");
			//herencia.clase=idclase.lexema
		}
		else 
			if (!tokenActual.getNombre().equals("LLave abre")){
				throw new Exception("Se esperaba extends o '{' y se encontro "+tokenActual.getNombre()+" en la linea "+tokenActual.getNroLinea() );
				//h.clase=object
			}
	}
	
	private void Miembros() throws Exception{
		
		if (tokenActual.getNombre().equals("public") 		||
			tokenActual.getNombre().equals("private")		||
			tokenActual.getNombre().equals("Id de clase")   ||
			tokenActual.getNombre().equals("static") 		||
			tokenActual.getNombre().equals("dynamic")) {
				Miembro();
				Miembros();
		}
		
		if(tokenActual.getNombre().equals("EOF"))
			throw new Exception("Se encontro EOF, falta '}' y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
		
		if(!tokenActual.getNombre().equals("Llave cierra")){
			throw new Exception("Se esperaba public/private/idClase/static/dynamic/} "
					+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
				
	}
	
	private void Miembro() throws Exception{
		if( tokenActual.getNombre().equals("public") || 
			tokenActual.getNombre().equals("private")) 
				Atributo();
		else 
			if(tokenActual.getNombre().equals("static") ||
			   tokenActual.getNombre().equals("dynamic"))
				Metodo();
		else 
			if(tokenActual.getNombre().equals("Id de clase"))
				Ctor();
		else 
			if(tokenActual.getNombre().equals("Llave cierra")){
			
		}
		else 
			if (tokenActual.getNombre().equals("EOF"))
				throw new Exception("Se encontro EOF en la linea "+tokenActual.getNroLinea()+". Error en la declaracoin de atributo, metodo o constructor. ");
			else
				throw new Exception("Error, "
						+ "Se esperaba public/private/static/dynamic/idClase "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}
	
	private void Atributo() throws Exception{
		Visibilidad();
		Tipo();
		ListaDecVars();
		match("Punto y coma");
	}
	
	private void Metodo() throws Exception{
		FormaMetodo();
		TipoMetodo();
		match("Id Metodo-Variable");
		ArgsFormales();
		Bloque();
	}
	
	private void Ctor() throws Exception{
		match("Id de clase");
		ArgsFormales();
		Bloque();
	}
	
	private void Visibilidad() throws Exception{
		match(tokenActual.getNombre()); 
	}
	
	private void FormaMetodo() throws Exception{
		match(tokenActual.getNombre());
	}
	
	private void Tipo() throws Exception{
		if(tokenActual.getNombre().equals("boolean") || 
		   tokenActual.getNombre().equals("char")    || 
		   tokenActual.getNombre().equals("int")) {
				match(tokenActual.getNombre());
				TipoAux();
		}
		else 
			if (tokenActual.getNombre().equals("Id de clase") || 
				tokenActual.getNombre().equals("String"))
					match(tokenActual.getNombre());
		else 
			throw new Exception("Error, "
					+ "se esperaba boolean/char/int/idClase/String "
					+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}
	
	private void TipoAux() throws Exception{
		if(tokenActual.getNombre().equals("Corchete abre")){
			match("Corchete abre");
			match("Corchete cierra");
		}
		else 
			if (tokenActual.getNombre().equals("Punto y coma")){
				throw new Exception("Falta idMetVar en la linea" + tokenActual.getNroLinea());
		}
		else 
			if (tokenActual.getNombre().equals("Coma"))
				Miembro();

	}
	
	private void ArgsFormales() throws Exception{ 
		match("Parentesis abre");
		AF();
		match("Parentesis cierra");
	}
	
	private void AF() throws Exception {
		if (tokenActual.getNombre().equals("boolean") ||
			tokenActual.getNombre().equals("char")	  ||
			tokenActual.getNombre().equals("int")     ||
			tokenActual.getNombre().equals("String"))
				ListaArgsFormales();
		else 
			if (!tokenActual.getNombre().equals("Parentesis cierra")) {
				throw new Exception("Error, "
						+ "Se esperaba boolean/char/int/String/) "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
		
	}

	private void ListaDecVars() throws Exception{
		match("Id Metodo-Variable");
		LDV();
		
	}
	
	private void LDV() throws Exception{
		if (tokenActual.getNombre().equals("Coma")) {
			match("Coma");
			ListaDecVars();
		}
		else
			if (!tokenActual.getNombre().equals("Punto y coma"))
				throw new Exception("Error,"
						+ " se esperaba ',' o ';' "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
				
	}
	
	private void ListaArgsFormales() throws Exception{
		ArgFormal();
		LAF();
	}
		
	private void LAF() throws Exception {
		if (tokenActual.getNombre().equals("Coma")) {
			match("Coma");
			ListaArgsFormales();
		}
		else 
			if (!tokenActual.getNombre().equals("Parentesis cierra")) 
				throw new Exception("Error,"
						+ " se esperaba ',' o ')' "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}

	private void ArgFormal() throws Exception{
		Tipo();
		match("Id Metodo-Variable");
	}
	
	private void TipoMetodo() throws Exception{

		if (tokenActual.getNombre().equals("boolean") ||
			tokenActual.getNombre().equals("char") 	  ||
			tokenActual.getNombre().equals("int") 	  ||
			tokenActual.getNombre().equals("String")  ||
			tokenActual.getNombre().equals("Id de clase"))
				Tipo();
		
		else 
			if (tokenActual.getNombre().equals("void"))
				match("void");
		else 
			throw new Exception("Error, "
					+ " se esperaba boolean/char/int/String/idClase/void "
					+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}
	
	private void Bloque() throws Exception{
		if(tokenActual.getNombre().equals("LLave abre")){
			match("LLave abre");
			Sentencias();
			match("Llave cierra");
		}
		else
			throw new Exception("Error, "
					+ " se esperaba '{' "
					+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}
	
	private void Sentencias() throws Exception{

		if (tokenActual.getNombre().equals("boolean") 			||
			tokenActual.getNombre().equals("char") 	  			||
			tokenActual.getNombre().equals("int") 	  			||
			tokenActual.getNombre().equals("String") 			||
			tokenActual.getNombre().equals("Id de clase") 		||
			tokenActual.getNombre().equals("Id Metodo-Variable")||
			tokenActual.getNombre().equals("Parentesis abre")  	||
			tokenActual.getNombre().equals("if") 	  			||
			tokenActual.getNombre().equals("while")   			||
			tokenActual.getNombre().equals("LLave abre")  		||
			tokenActual.getNombre().equals("return") 	 		||
			tokenActual.getNombre().equals("this") 	  			||	
			tokenActual.getNombre().equals("Punto y coma")) {
				Sentencia();
				Sentencias();
		}
		else 
			if (!tokenActual.getNombre().equals("Llave cierra")) 
				throw new Exception("Error, "
						+ "se esperaba boolean/char/int/String/idClase/idMetVar/'('/if/while/{/return/this/;/} en la linea "+ tokenActual.getNroLinea()
						+ " y se encontro " + tokenActual.getNombre());
		
	}
	
	private void Sentencia() throws Exception{
		if (tokenActual.getNombre().equals("Punto y coma"))
			match("Punto y coma");
		else 
			if (tokenActual.getNombre().equals("Id Metodo-Variable") ||
				tokenActual.getNombre().equals("this")) { 
					Asignacion();
					match("Punto y coma");
			}
		else 
			if (tokenActual.getNombre().equals("Parentesis abre")) {
				sentenciaLlamada();
				match("Punto y coma");
			}
		else {
			if (tokenActual.getNombre().equals("boolean") 			||
				tokenActual.getNombre().equals("char") 	  			||
				tokenActual.getNombre().equals("int") 	  			||
				tokenActual.getNombre().equals("String") 			||
				tokenActual.getNombre().equals("Id de clase")){
					Tipo();
					ListaDecVars();
					match("Punto y coma");
				}
			else 
				if (tokenActual.getNombre().equals("if")) {
					match("if");
					match("Parentesis abre");
					Expresion();
					match("Parentesis cierra");
					Sentencia();
					S();
				}
			else 
				if (tokenActual.getNombre().equals("while")) {
					match("while");
					match("Parentesis abre");
					Expresion();
					match("Parentesis cierra");
					Sentencia();
				}
			else 
				if (tokenActual.getNombre().equals("LLave abre")) 
					Bloque();
			else 
				if (tokenActual.getNombre().equals("return")) {
					match("return");
					Expresiones();
					match("Punto y coma");
			}
			else
				throw new Exception("Error, "
						+ "se esperaba ;/ idMetVar / this / ( / boolean/ char"
						+ "/ int / string / idClase / if / while / { / return, en la linea "+ tokenActual.getNroLinea() +
						 " y se encontro " +tokenActual.getNombre());
		}
	}
	
	private void sentenciaLlamada() throws Exception {
		match("Parentesis abre");
		Expresion();
		match("Parentesis cierra");		
	}

	private void Asignacion() throws Exception {		
		if(tokenActual.getNombre().equals("this")){
			match("this");
			Encadenados();
			match("Asignacion");
			Expresion();
		}
		else 
			if(tokenActual.getNombre().equals("Id Metodo-Variable")){
				match("Id Metodo-Variable");
				Encadenados();
				match("Asignacion");
				Expresion();
			}
		else 
			throw new Exception("Error, se esperaba IdMetVar/this y se encontro "+tokenActual.getNombre()+"  en la linea "+tokenActual.getNroLinea());	
	}

	private void Expresiones() throws Exception{
		
		if( tokenActual.getNombre().equals("Signo +") 			||
			tokenActual.getNombre().equals("Signo -") 	  		||
			tokenActual.getNombre().equals("Not")	 	  		||
			tokenActual.getNombre().equals("Parentesis abre") 	||
			tokenActual.getNombre().equals("Id de clase")		||
			tokenActual.getNombre().equals("Id Metodo-Variable")||
			tokenActual.getNombre().equals("new") 	  			||
			tokenActual.getNombre().equals("Entero") 			||
			tokenActual.getNombre().equals("Caracter")			||
			tokenActual.getNombre().equals("String") 			||
			tokenActual.getNombre().equals("false") 	  		||
			tokenActual.getNombre().equals("null") 	  			||
			tokenActual.getNombre().equals("true") 				||
			tokenActual.getNombre().equals("this"))		
				Expresion();
		else 
			if (!tokenActual.getNombre().equals("Punto y coma"))
				throw new Exception("Error, "
						+ "se esperaba + - ! ( idClase idMetVar new "
						+ "entero caracter string false null true this, "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}
	

	private void Expresion() throws Exception {
			expOr();
	}
	
	private void S() throws Exception{
		if(tokenActual.getNombre().equals("else")){
			match("else");
			Sentencia();
		}
		else{
			List<String> siguientes = new ArrayList<String>();
			
			siguientes.add("int");
			siguientes.add("char");
			siguientes.add("Id Metodo-Variable");
			siguientes.add("Punto y coma");
			siguientes.add("Punto");
			siguientes.add("boolean");
			siguientes.add("Parentesis cierra");
			siguientes.add("String");
			siguientes.add("Id de clase");
			siguientes.add("if");
			siguientes.add("while");
			siguientes.add("LLave abre");
			siguientes.add("Llave cierra");
			siguientes.add("return");
			
			
			if (!siguientes.contains(tokenActual.getNombre())) 
				throw new Exception("Error, "
						+ "se esperaba 'else' "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );

		}
	}
	
	private void expOr() throws Exception{
		expAnd();
		EO();
	}
	
	
	private void EO() throws Exception {
		if(tokenActual.getNombre().equals("Or")){
			match("Or");
			expAnd();
			EO();
		}
		else{
			List<String> siguientes = new ArrayList<String>();
			siguientes.add("Parentesis cierra");
			siguientes.add("Punto y coma");
			siguientes.add("Punto");
			siguientes.add("Coma");
			siguientes.add("Corchete cierra");

			if (!siguientes.contains(tokenActual.getNombre()))
				throw new Exception("Error, se esperaba || y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
		}
	}

	private void expAnd() throws Exception{
		expIg();
		EA();
	}
	
	private void EA() throws Exception {
		if (tokenActual.getNombre().equals("And")) {
			match("And");
			expIg();
			EA();
		}
		else{
			List<String> siguientes = new ArrayList<String>();
			siguientes.add("Or");
			siguientes.add("Parentesis cierra");
			siguientes.add("Punto y coma");
			siguientes.add("Punto");
			siguientes.add("Coma");
			siguientes.add("Corchete cierra");
			if (!siguientes.contains(tokenActual.getNombre()))
				throw new Exception("Error, se esperaba && y se encontro "+tokenActual.getNombre()+" en linea "+ tokenActual.getNroLinea());
			
		}
	}

	private void expIg() throws Exception{
		expComp();
		EI();
	}
	
	private void EI() throws Exception {
		List<String> siguientes = new ArrayList<String>();

		if(tokenActual.getNombre().equals("Comparacion") ||
		   tokenActual.getNombre().equals("Distinto")){
			opIg();
			expComp();
			EI();
		}
		else{
			siguientes.add("And");
			siguientes.add("Or");
			siguientes.add("Parentesis cierra");
			siguientes.add("Punto y coma");
			siguientes.add("Punto");
			siguientes.add("Coma");
			siguientes.add("Corchete cierra");
			if (!siguientes.contains(tokenActual.getNombre()))
				throw new Exception("Error, se esperaba == o != y se encontro "+tokenActual.getNombre()+" en linea "+ tokenActual.getNroLinea());
			//verrrrrrrr stooooooo mikkkkkk
				
		}
		
	}

	private void opIg() throws Exception {
		if(tokenActual.getNombre().equals("Comparacion"))
			match("Comparacion");
		else 
			if(tokenActual.getNombre().equals("Distinto"))
				match("Distinto");
		else
			throw new Exception("Error, se esperaba == o != y se encontro "+tokenActual.getNombre()+" en la linea "+tokenActual.getNroLinea());
	}

	private void expComp() throws Exception{
		expAd();
		EC();
	}
	
	private void EC() throws Exception {
		List<String> siguientes = new ArrayList<String>();

		if (tokenActual.getNombre().equals("Menor") 		 ||
			tokenActual.getNombre().equals("Mayor") 	  	 ||
			tokenActual.getNombre().equals("Menor o igual")	 ||
			tokenActual.getNombre().equals("Mayor o igual")){
				opComp();
				expAd();
		}
		else {
			
			siguientes.add("And");
			siguientes.add("Or");
			siguientes.add("Parentesis cierra");
			siguientes.add("Punto y coma");
			siguientes.add("Punto");
			siguientes.add("Coma");
			siguientes.add("Corchete cierra");
			siguientes.add("Comparacion");
			siguientes.add("Distinto");
			if (!siguientes.contains(tokenActual.getNombre())) 
				throw new Exception("Error en la declaracion de EC proveniente de expComp. Expresion erronea en linea "+ tokenActual.getNroLinea());
				
		}
	}

	private void opComp() throws Exception {
		switch(tokenActual.getNombre()){
			case "Mayor": 
				match("Mayor"); 
				break;
			case "Menor": 
				match("Menor");
				break; 
			case "Mayor o igual": 
				match("Mayor o igual"); 
				break;
			case "Menor o igual": 
				match("Menor o igual"); 
				break;
			default:
				throw new Exception("Error, se esperaba < > <= >= y se encontro "+tokenActual.getNombre()+"  en la linea " + tokenActual.getNroLinea());
		}
		
	}

	private void expAd() throws Exception{
		expMul();
		EM();
	}
	
	private void EM() throws Exception {
	if(tokenActual.getNombre().equals("Signo +") ||
	   tokenActual.getNombre().equals("Signo -")){
		opAd();
		expMul();
		EM();
	}
	else {
		List<String> siguientes = new ArrayList<String>();
		
		siguientes.add("And");
		siguientes.add("Or");
		siguientes.add("Parentesis cierra");
		siguientes.add("Punto y coma");
		siguientes.add("Punto");
		siguientes.add("Coma");
		siguientes.add("Corchete cierra");
		siguientes.add("Comparacion");
		siguientes.add("Distinto");
		siguientes.add("Menor");
		siguientes.add("Mayor");
		siguientes.add("Menor o igual");
		siguientes.add("Mayor o igual");
		
		if (!siguientes.contains(tokenActual.getNombre()))
			throw new Exception("Error en la declaracion de EM proveniente de expAd. Expresion erronea en linea "+ tokenActual.getNroLinea());
	}	
	}

	private void opAd() throws Exception {
		switch(tokenActual.getNombre()){
		case "Signo +": 
			match("Signo +"); 
			break; 
		case "Signo -": 
			match("Signo -"); 
			break;

		default:
			throw new Exception("Error, se esperaba +/' y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
	}
		
	}

	private void expMul() throws Exception{
		expUn();
		EU();
	}
	
	private void EU() throws Exception {
		List<String> siguientes = new ArrayList<String>();

		if (tokenActual.getNombre().equals("Signo *")	||
			tokenActual.getNombre().equals("Signo /")) {
				opMul();
				expUn();
				EU();
		}
		else {
			
			siguientes.add("Signo +");
			siguientes.add("Signo -");
			siguientes.add("And");
			siguientes.add("Or");
			siguientes.add("Parentesis cierra");
			siguientes.add("Punto y coma");
			siguientes.add("Punto");
			siguientes.add("Coma");
			siguientes.add("Corchete cierra");
			siguientes.add("Comparacion");
			siguientes.add("Distinto");
			siguientes.add("Menor");
			siguientes.add("Mayor");
			siguientes.add("Menor o igual");
			siguientes.add("Mayor o igual");
			
			if(!siguientes.contains(tokenActual.getNombre()))
				throw new Exception("Error en la declaracion de EU proveniente de expMul. Expresion erronea en linea "+ tokenActual.getNroLinea());
		}
	}

	private void expUn() throws Exception{		
		
		if (tokenActual.getNombre().equals("Signo +") ||
			tokenActual.getNombre().equals("Signo -") ||
			tokenActual.getNombre().equals("Not"))	{
				opUn();
				expUn();
		}
		else {
			
			if (tokenActual.getNombre().equals("this") ||
				tokenActual.getNombre().equals("null") ||
				tokenActual.getNombre().equals("true") ||
				tokenActual.getNombre().equals("false") ||
				tokenActual.getNombre().equals("Entero") ||
				tokenActual.getNombre().equals("String") ||
				tokenActual.getNombre().equals("Parentesis abre") ||
				tokenActual.getNombre().equals("Id Metodo-Variable") ||
				tokenActual.getNombre().equals("Id de clase") ||
				tokenActual.getNombre().equals("new") ||
				tokenActual.getNombre().equals("Caracter"))
					Operando();
			else
				throw new Exception("Error en la declaracion de expUn. Expresion erronea en linea "+ tokenActual.getNroLinea());
		}
	}
	
	private void opMul() throws Exception{
		switch(tokenActual.getNombre()){
			case "Signo *": 
				match("Signo *"); 
				break;
			case "Signo /": 
				match("Signo /"); 
				break;

		default:
			throw new Exception("Error, se esperaba * / y se encontro "+tokenActual.getNombre()+" en la linea " + tokenActual.getNroLinea());
		}
	}

	private void Operando() throws Exception {
		
		if( tokenActual.getNombre().equals("null") 			||
			tokenActual.getNombre().equals("true") 	  		||
			tokenActual.getNombre().equals("false") 	  	||
			tokenActual.getNombre().equals("Entero") 		||
			tokenActual.getNombre().equals("Caracter") 		||
			tokenActual.getNombre().equals("String"))
				Literal();
		else{
			if (tokenActual.getNombre().equals("Parentesis abre") 	||
				tokenActual.getNombre().equals("this") 	  			||
				tokenActual.getNombre().equals("Id Metodo-Variable")||
				tokenActual.getNombre().equals("Id de clase") 		||
				tokenActual.getNombre().equals("new"))
					Primario();
			else
				throw new Exception("Error, se esperaba null/ true / false/ entero/ caracter/ string/"
						+ " ( / this / idMetVar / idClase / new, "
									+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
	}
	
	private void Literal() throws Exception{
		match(tokenActual.getNombre());
	}

	private void opUn() throws Exception {
		switch(tokenActual.getNombre()){
			case "Signo +": 
				match("Signo +");
				break;
			case "Signo -": 
				match("Signo -");
				break;
			case "Not":		
				match("Not");
				break;
		default:
			throw new Exception("Error, se esperaba + - ! y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
		
		}
	}
	
	private void Primario() throws Exception{
		switch(tokenActual.getNombre()){
			case "Parentesis abre":
				match("Parentesis abre");
				Expresion();
				match("Parentesis cierra");
				Encadenados();
				break;
			case "this":
				match("this");
				Encadenados();
				break;
			case "Id Metodo-Variable":
				match("Id Metodo-Variable");
				IMV();
				break;
			case "Id de clase":
				match("Id de clase");
				match("Punto");
				llamadaMetodo();
				Encadenados();
				break;
			case "new":
				match("new");
				N();
				break;
		}
	}

	private void N() throws Exception { 
		if(tokenActual.getNombre().equals("Id de clase")){
			match("Id de clase");
			argsActuales();
			Encadenados();
		}
		else 
			if (tokenActual.getNombre().equals("boolean") ||
				tokenActual.getNombre().equals("char")    ||
				tokenActual.getNombre().equals("int")){
					match(tokenActual.getNombre());
					match("Corchete abre");
					Expresion();
					match("Corchete cierra");
					Encadenados();
		}
		else 
			throw new Exception("Error, "
					+ "se esperaba idClase/ boolean/ char/int "
					+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			
	}

	private void argsActuales() throws Exception {
		match("Parentesis abre");
		ListaExpsAus();
		match("Parentesis cierra");
	}
	
	private void ListaExpsAus() throws Exception {
		if (tokenActual.getNombre().equals("Signo +") 			||
			tokenActual.getNombre().equals("Signo -") 	  		||
			tokenActual.getNombre().equals("Not") 	  			||
			tokenActual.getNombre().equals("Parentesis abre") 	||
			tokenActual.getNombre().equals("Id de clase")		||
			tokenActual.getNombre().equals("Id Metodo-Variable")||
			tokenActual.getNombre().equals("new") 	  			||
			tokenActual.getNombre().equals("Entero") 			||
			tokenActual.getNombre().equals("Caracter")			||
			tokenActual.getNombre().equals("String") 			||
			tokenActual.getNombre().equals("false") 	  		||
			tokenActual.getNombre().equals("null") 	  			||
			tokenActual.getNombre().equals("true") 				||
			tokenActual.getNombre().equals("this"))
			listaExps();
		else 
			if (!tokenActual.getNombre().equals("Parentesis cierra")){
				throw new Exception("Error,"
						+ "se esperaba + - ! ( idClase idMetVar new entero caracter string false null true this "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
	}

	private void listaExps() throws Exception {
		Expresion();
		LE();
	}

	private void LE() throws Exception {
		if(tokenActual.getNombre().equals("Coma")){
			match("Coma");
			listaExps();
		}
		else 
			if(!tokenActual.getNombre().equals(("Parentesis cierra"))){
				throw new Exception("Error,"
						+ "se esperaba , o ) "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
	}

	private void llamadaMetodo() throws Exception {
		match("Id Metodo-Variable");
		argsActuales();
		Encadenados();
	}

	private void IMV() throws Exception {
		if(tokenActual.getNombre().equals("Parentesis abre")){
			argsActuales();
		}
		Encadenados();
	}

	private void Encadenados() throws Exception {

		if(tokenActual.getNombre().equals("Punto") || 
		   tokenActual.getNombre().equals("Corchete abre"))
			Encadenado();
		else 
			if(tokenActual.getNombre().equals("Asignacion")){}
					
		else{
			List<String> siguientes = new ArrayList<String>();
			siguientes.add("Signo *");
			siguientes.add("Id Metodo-Variable");
			siguientes.add("Signo /");
			siguientes.add("Signo +");
			siguientes.add("Signo -");
			siguientes.add("Menor");
			siguientes.add("Mayor");
			siguientes.add("Menor o igual");
			siguientes.add("Mayor o igual");
			siguientes.add("Comparacion");
			siguientes.add("Distinto");
			siguientes.add("And");
			siguientes.add("Or");
			siguientes.add("Parentesis cierra");
			siguientes.add("Punto y coma");
			siguientes.add("Coma");
			siguientes.add("Corchete cierra");
		
			if (siguientes.contains(tokenActual.getNombre())){}
			else
				throw new Exception("Error,"
						+ "se esperaba . [ o = "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			
		}
	}
	
	private void Encadenado() throws Exception{
		if(tokenActual.getNombre().equals("Punto")){
			match("Punto");
			match("Id Metodo-Variable");
			IMV();
		}
		else 
			if (tokenActual.getNombre().equals("Corchete abre")) {
				match("Corchete abre");
				Expresion();
				match("Corchete cierra");
				Encadenados();
			}
			else 
				throw new Exception("Error,"
						+ " se esperaba . [ "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
	}

	
}