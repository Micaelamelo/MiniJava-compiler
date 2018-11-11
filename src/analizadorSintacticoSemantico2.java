	import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

	public class analizadorSintacticoSemantico2 {
		

		private analizadorLexico2 AL;
		private Token tokenActual;
		private TablaDeSimbolos tabla;
		private Generador generador;
		
		public analizadorSintacticoSemantico2(String archivo, String salida) throws Exception{
			AL= new analizadorLexico2(archivo);
			tabla=new TablaDeSimbolos();
			tokenActual= AL.getToken();
			
			generador= new Generador(archivo);
			
			Inicial();
			tabla.chequearTodo();
			tabla.consolidar2();
			tabla.chequearBloques();

			tabla.generar();
			
			for(Clase c: tabla.getClases()){
				if(!c.getNombre().equals("Object") && !c.getNombre().equals("System")){
				//	System.out.println("El constructor de la clase es: "+c.getConstructor().getNombre());
					System.out.println("La clase "+c.getNombre()+" tiene : ");
					
					for(Atributo a: c.getAtributos().values())
						System.out.println("Tiene atributo "+ a.getNombre());
					for(Metodo m: c.getMetodos().values()){
						System.out.println("Tiene metodo "+ m.getNombre());
						for(Parametro p: m.getParametros())
							System.out.println("Tiene parametro "+ p.getNombre()+ " de tipo "+ p.getTipo().getTipo());
						System.out.println("Y las variables locales son ");
						
						if(m.getBloque().getAtributosLocales().size()!=0){
							for(Atributo a: m.getBloque().getAtributosLocales()){
								System.out.println("Atributo "+a.getNombre());
							}
						}
					}
				}
				
				System.out.println("\n");
			}
			
			System.out.println("La clase con Main es "+ tabla.getClaseConMain().getNombre());
			

			


		}
		
		private Token match(String nombre) throws Exception{
			System.out.print ("TOKEN ACTUAL \n" +tokenActual.getNombre()+ " CON LEXEMA "+tokenActual.getLexema());
			System.out.println (" SE QUIERE MACHEAR CON \n" +nombre);
			System.out.println ("-----------------------------------");
			
			if(nombre.equals(tokenActual.getNombre())) {
					Token tokenAnterior= tokenActual;
					tokenActual= AL.getToken();
					return tokenAnterior;				
			}
			else{
				throw new Exception("Error se espera "+ nombre +" en la linea "+ tokenActual.getNroLinea() );
			}
		
		}
		
		//Comienzo de las producciones
		
		private void Inicial() throws Exception{
			Clases();
		}
		
		private void Clases() throws Exception{
			Clase();
			ClasesAux();
		}
		
		private void Clase() throws Exception{
			match("class");
			
			Clase c=new Clase(tokenActual.getLexema(), null, tabla, tokenActual.getNroLinea());
			
			match("Id de clase");
			
			String papa= Herencia();
			c.setHeredaDe(papa);
					
			tabla.insertarClase(c);
			tabla.setClaseActual(c);
			
			Herencia();
			
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
		 	
		private String Herencia() throws Exception{
			
			if(tokenActual.getNombre().equals("extends")){
				match("extends");
				String heredaDe= tokenActual.getLexema();
				match("Id de clase");
				return heredaDe;
			}
			else if (tokenActual.getNombre().equals("LLave abre")){
					return "Object"; //si la clase no extiende a nada, su padre sera Object
				}
			else 
				throw new Exception("Se esperaba extends o '{' y se encontro "+tokenActual.getNombre()+" en la linea "+tokenActual.getNroLinea() );
				
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
			
			String v=Visibilidad();
		//	System.out.println("visibilidad es "+v);
			
			Tipo t=Tipo();
			
		//	System.out.println("tipo es "+t.getTipo());
			
			ListaDecVars(v,t, null); 	// lista dec atributos, y lo q hice todo ahora pasarlo a lista dec atributos; y listadec
			match("Punto y coma");
		}
		
		private void Metodo() throws Exception{
			String f=FormaMetodo();
			TipoMetodo t=TipoMetodo();
			
			Metodo m= new Metodo(tokenActual.getLexema(), tabla, t, f, tokenActual.getNroLinea()); 
			
			tabla.getClaseActual().insertarMetodo(m);
			tabla.getClaseActual().setUnidadActual(m);
			
			match("Id Metodo-Variable");
			ArgsFormales();
			
			NodoBloque bloque= Bloque();
			tabla.getClaseActual().getUnidadActual().setBloque(bloque);
		}
		
		private void Ctor() throws Exception{
			Constructor c= new Constructor(tokenActual.getLexema(), tabla,tokenActual.getNroLinea());
			tabla.getClaseActual().insertarConstructor(c);
			tabla.getClaseActual().setUnidadActual(c);
			
			match("Id de clase");
			ArgsFormales();
			
			NodoBloque bloque= Bloque();
			tabla.getClaseActual().getUnidadActual().setBloque(bloque);
		}
		
		private String Visibilidad() throws Exception{
				return match(tokenActual.getNombre()).getNombre(); 
		
		}
		
		private String FormaMetodo() throws Exception{
		
			return	match(tokenActual.getNombre()).getNombre();
		}
		
		private Tipo Tipo() throws Exception{
			//Tipo primitivo
		
			if(tokenActual.getNombre().equals("boolean")) {
					match(tokenActual.getNombre());
					boolean t=TipoAux();
					if (t==true)
							return new TABoolean("TAboolean");
					else
							return new TBoolean("boolean");
			}
			else 
				if (tokenActual.getNombre().equals("char")){
					match(tokenActual.getNombre());
					boolean t=TipoAux();
					if (t==true)
							return new TAChar("TAchar");
					else
							return new TChar("char");
			}
			else 
				if (tokenActual.getNombre().equals("int")){
					match(tokenActual.getNombre());
					boolean t=TipoAux();
					if (t==true)
							return new TAInt("TAint");
					else
							return new TInt("int");
			}
			//Tipo referencia
			else 
				if (tokenActual.getNombre().equals("String")){
						Tipo ts= new TString("String");
						match(tokenActual.getNombre());
						return ts;
			}
			else 
				if (tokenActual.getNombre().equals("Id de clase")){
					Tipo tc= new TIdClase(tabla, tokenActual.getLexema());
					match(tokenActual.getNombre());
				return tc;
			}
			else 
				throw new Exception("Error, "
						+ "se esperaba boolean/char/int/idClase/String "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
		
		private boolean TipoAux() throws Exception{
			boolean esArreglo=false;
			if(tokenActual.getNombre().equals("Corchete abre")){
				match("Corchete abre");
				match("Corchete cierra");
				esArreglo=true;
			}
			else 
				if (tokenActual.getNombre().equals("Punto y coma")){
					throw new Exception("Falta idMetVar en la linea" + tokenActual.getNroLinea());
			}
			else 
				if (tokenActual.getNombre().equals("Coma"))
					Miembro();
			return esArreglo;
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
				tokenActual.getNombre().equals("String")  ||
				tokenActual.getNombre().equals("Id de clase"))
					ListaArgsFormales();
			else 
				if (!tokenActual.getNombre().equals("Parentesis cierra")) {
					throw new Exception("Efrror, "
							+ "Se esperaba boolean/char/int/String/) "
							+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			}
			
		}

		private void ListaDecVars(String v, Tipo t, NodoDecVars ndv) throws Exception{
			Atributo a= new Atributo(tokenActual.getLexema(), t, v, tokenActual.getNroLinea());
			
			if(v!= null)
				tabla.getClaseActual().insertarAtributo(a);
			else{
				ndv.insertarVariable(a);
			}
			
			match("Id Metodo-Variable");
			LDV(v,t, ndv);
			
		}
		
		private void LDV(String v, Tipo t, NodoDecVars ndv) throws Exception{
			if (tokenActual.getNombre().equals("Coma")) {
				match("Coma");
				ListaDecVars(v,t, ndv);
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
			Tipo t=Tipo();
			
			tabla.getClaseActual().getUnidadActual().insertarParametro(new Parametro(tokenActual.getLexema(), t, tokenActual.getNroLinea()));
			match("Id Metodo-Variable");
			
		}
		
		private TipoMetodo TipoMetodo() throws Exception{

			if (tokenActual.getNombre().equals("boolean") ||
				tokenActual.getNombre().equals("char") 	  ||
				tokenActual.getNombre().equals("int") 	  ||
				tokenActual.getNombre().equals("String")  ||
				tokenActual.getNombre().equals("Id de clase"))
					return Tipo();
			
			else 
				if (tokenActual.getNombre().equals("void")){
					TipoMetodo tv= new TVoid("void");
					match("void");
					return tv;
				}
			else 
				throw new Exception("Error, "
						+ " se esperaba boolean/char/int/String/idClase/void "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
		
		private NodoBloque Bloque() throws Exception{
			if(tokenActual.getNombre().equals("LLave abre")){
				match("LLave abre");
				NodoBloque Nb= new NodoBloque(tokenActual, tabla.getBloqueActual(), tabla.getClaseActual().getUnidadActual(), tabla.getClaseActual());
				tabla.setBloqueActual(Nb);
				Sentencias(Nb);
				match("Llave cierra");
				tabla.setBloqueActual(Nb.getBloquePadre());
				return Nb;
			}
			else
				throw new Exception("Error, "
						+ " se esperaba '{' "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
		
		private void Sentencias(NodoBloque nb) throws Exception{

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
					NodoSentencia ns= Sentencia();
					if(ns!=null){
						nb.insertarSentencia(ns);
					}
					Sentencias(nb);
			}
			else 
				if (!tokenActual.getNombre().equals("Llave cierra")) 
					throw new Exception("Error, "
							+ "se esperaba boolean/char/int/String/idClase/idMetVar/'('/if/while/{/return/this/;/} en la linea "+ tokenActual.getNroLinea()
							+ " y se encontro " + tokenActual.getNombre());
			
		}
		
		private NodoSentencia Sentencia() throws Exception{
			if (tokenActual.getNombre().equals("Punto y coma")){
				Token t= match("Punto y coma");
				return new NodoPuntoYcoma(t);
			}
			else 
				if (tokenActual.getNombre().equals("Id Metodo-Variable") ||
					tokenActual.getNombre().equals("this")) { 
						NodoAsignacion asignacion=Asignacion();
						match("Punto y coma");
						return asignacion;
				}
			else 
				if (tokenActual.getNombre().equals("Parentesis abre")) {
					NodoSentenciaLlamada llamada= sentenciaLlamada();
					match("Punto y coma");
					return llamada;
				}
			else {
				if (tokenActual.getNombre().equals("boolean") 			||
					tokenActual.getNombre().equals("char") 	  			||
					tokenActual.getNombre().equals("int") 	  			||
					tokenActual.getNombre().equals("String") 			||
					tokenActual.getNombre().equals("Id de clase")){
						Tipo t= Tipo();
						NodoDecVars ndv= new NodoDecVars(tabla.getBloqueActual(), tokenActual, tabla);
						
						ListaDecVars(null, t, ndv);
						match("Punto y coma");
						return ndv;
					}
				else 
					if (tokenActual.getNombre().equals("if")) {
						Token tIf= match("if");
						match("Parentesis abre");
						NodoExpresion ne= Expresion();
						match("Parentesis cierra");
						NodoSentencia ns=Sentencia();
						NodoSentencia nselse= S();
						return new NodoIfElse(ne, ns, nselse, tIf);
					}
				else 
					if (tokenActual.getNombre().equals("while")) {
						Token tWhile= match("while");
						match("Parentesis abre");
						NodoExpresion ne= Expresion();
						match("Parentesis cierra");
						NodoSentencia ns= Sentencia();
						return new NodoWhile(ne, ns, tWhile);
					}
				else 
					if (tokenActual.getNombre().equals("LLave abre")) 
						return Bloque();
				else 
					if (tokenActual.getNombre().equals("return")) {
						Token tReturn= match("return");
						NodoExpresion ne= Expresiones();
						match("Punto y coma");
						return new NodoReturn(ne, tabla.getClaseActual().getUnidadActual().getNombre() , tReturn, tabla.getClaseActual());
				}
				else
					throw new Exception("Error, "
							+ "se esperaba ;/ idMetVar / this / ( / boolean/ char"
							+ "/ int / string / idClase / if / while / { / return, en la linea "+ tokenActual.getNroLinea() +
							 " y se encontro " +tokenActual.getNombre());
			}
		}
		
		private NodoSentenciaLlamada sentenciaLlamada() throws Exception {
			match("Parentesis abre");
			NodoOperandoPrimario p = Primario();
			
			NodoSentenciaLlamada ret= new NodoSentenciaLlamada(tokenActual, p); //no iria token actual D:
			
			ret.setSentencia(p);
		//	ret.setToken(tokenActual);
	
			match("Parentesis cierra");		
			return ret;
		}

		private NodoAsignacion Asignacion() throws Exception {		
			if(tokenActual.getNombre().equals("this")){
		//		match("this");
			//	Encadenados();
				NodoAcceso NID = LIT();
				Token t=match("Asignacion");
				NodoExpresion NE = Expresion();
				return new NodoAsignacion(NID, NE, t);
			}
			else 
				if(tokenActual.getNombre().equals("Id Metodo-Variable")){
				//	match("Id Metodo-Variable");
					NodoAcceso NID = LIV();
				//	Encadenados();
					Token t=match("Asignacion");
					NodoExpresion NE = Expresion();
					return new NodoAsignacion(NID, NE, t);
				}
			else 
				throw new Exception("Error, se esperaba IdMetVar/this y se encontro "+tokenActual.getNombre()+"  en la linea "+tokenActual.getNroLinea());	
		}

		private NodoAcceso LIT() throws Exception {
			Token t = match("this");
			NodoAcceso NID = new NodoThis(t, null, tabla.getClaseActual(),tabla.getClaseActual().getUnidadActual(), tabla);
			Encadenados(NID);
			return NID;
		}
		
		private NodoAcceso LIV() throws Exception {
			Token t = match("Id Metodo-Variable");
			NodoAcceso NID = new NodoVar(t, null, tabla.getBloqueActual());
			Encadenados(NID);
			return NID;
		}
		

		private NodoExpresion Expresiones() throws Exception{
			
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
					return Expresion();
			else 
				if (!tokenActual.getNombre().equals("Punto y coma"))
					throw new Exception("Error, "
							+ "se esperaba + - ! ( idClase idMetVar new "
							+ "entero caracter string false null true this, "
							+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			return null;
		}
		

		private NodoExpresion Expresion() throws Exception {
				return expOr();
		}
		
		private NodoSentencia S() throws Exception{
			if(tokenActual.getNombre().equals("else")){
				match("else");
				return Sentencia();
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
			
			return null;
		}
		
		private NodoExpresion expOr() throws Exception{
			NodoExpresion ne= expAnd();
			return EO(ne);
		}
		
		
		private NodoExpresion EO(NodoExpresion ne) throws Exception {
			if(tokenActual.getNombre().equals("Or")){
				Token tOr=match("Or");
				NodoExpresion na= expAnd();
				NodoExpresionBin neb= new NodoExpresionBin(tOr, ne, na);
				return EO(neb);
			}
			else{
				List<String> siguientes = new ArrayList<String>();
				siguientes.add("Parentesis cierra");
				siguientes.add("Punto y coma");
				siguientes.add("Punto");
				siguientes.add("Coma");
				siguientes.add("Corchete cierra");

				if (siguientes.contains(tokenActual.getNombre())) return ne;
				else
					throw new Exception("Error, se esperaba || y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
			}
		}

		private NodoExpresion expAnd() throws Exception{
			NodoExpresion n= expIg();
			return EA(n);
		}
		
		private NodoExpresion EA(NodoExpresion n) throws Exception {
			if (tokenActual.getNombre().equals("And")) {
				Token t= match("And");
				NodoExpresion ni= expIg();
				NodoExpresionBin neb= new NodoExpresionBin(t, n, ni);
				return EA(neb);
			}
			else{
				List<String> siguientes = new ArrayList<String>();
				siguientes.add("Or");
				siguientes.add("Parentesis cierra");
				siguientes.add("Punto y coma");
				siguientes.add("Punto");
				siguientes.add("Coma");
				siguientes.add("Corchete cierra");
				if (siguientes.contains(tokenActual.getNombre()))
					return n;
				else
					throw new Exception("Error, se esperaba && y se encontro "+tokenActual.getNombre()+" en linea "+ tokenActual.getNroLinea());
				
			}
		}

		private NodoExpresion expIg() throws Exception{
			NodoExpresion n= expComp();
			return EI(n);
		}
		
		private NodoExpresion EI(NodoExpresion n) throws Exception {
			List<String> siguientes = new ArrayList<String>();

			if(tokenActual.getNombre().equals("Comparacion") ||
			   tokenActual.getNombre().equals("Distinto")){
				Token t= opIg();
				NodoExpresion nec= expComp();
				NodoExpresionBin neb= new NodoExpresionBin(t, n, nec);
				return EI(neb);
			}
			else{
				siguientes.add("And");
				siguientes.add("Or");
				siguientes.add("Parentesis cierra");
				siguientes.add("Punto y coma");
				siguientes.add("Punto");
				siguientes.add("Coma");
				siguientes.add("Corchete cierra");
				if (siguientes.contains(tokenActual.getNombre()))
					return n;
				else
					throw new Exception("Error, se esperaba == o != y se encontro "+tokenActual.getNombre()+" en linea "+ tokenActual.getNroLinea());
			}
			
		}

		private Token opIg() throws Exception {
			if(tokenActual.getNombre().equals("Comparacion"))
				return match("Comparacion");
			else 
				if(tokenActual.getNombre().equals("Distinto"))
					return match("Distinto");
			else
				throw new Exception("Error, se esperaba == o != y se encontro "+tokenActual.getNombre()+" en la linea "+tokenActual.getNroLinea());
		}

		private NodoExpresion expComp() throws Exception{
			NodoExpresion n= expAd();
			return EC(n);
		}
		
		private NodoExpresion EC(NodoExpresion n) throws Exception {
			List<String> siguientes = new ArrayList<String>();

			if (tokenActual.getNombre().equals("Menor") 		 ||
				tokenActual.getNombre().equals("Mayor") 	  	 ||
				tokenActual.getNombre().equals("Menor o igual")	 ||
				tokenActual.getNombre().equals("Mayor o igual")){
					Token t=opComp();
					NodoExpresion na= expAd();
					return new NodoExpresionBin(t, n, na);
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
				if (siguientes.contains(tokenActual.getNombre())) 
					return n;
				else
					throw new Exception("Error en la declaracion de EC proveniente de expComp. Expresion erronea en linea "+ tokenActual.getNroLinea());
					
			}		}

		private Token opComp() throws Exception {
			switch(tokenActual.getNombre()){
				case "Mayor": 
					return match("Mayor"); 
				case "Menor": 
					return match("Menor");
				case "Mayor o igual": 
					return match("Mayor o igual"); 
				case "Menor o igual": 
					return match("Menor o igual"); 
				default:
					throw new Exception("Error, se esperaba < > <= >= y se encontro "+tokenActual.getNombre()+"  en la linea " + tokenActual.getNroLinea());
			}
			
		}

		private NodoExpresion expAd() throws Exception{
			NodoExpresion n= expMul();
			return EM(n);
		}
		
		private NodoExpresion EM(NodoExpresion n) throws Exception {
		if(tokenActual.getNombre().equals("Signo +") ||
		   tokenActual.getNombre().equals("Signo -")){
			Token t= opAd();
			NodoExpresion nem= expMul();
			NodoExpresionBin neb= new NodoExpresionBin(t, n, nem);
			return EM(neb);
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
			
			if (siguientes.contains(tokenActual.getNombre()))
				return n;
			else
				throw new Exception("Error en la declaracion de EM proveniente de expAd. Expresion erronea en linea "+ tokenActual.getNroLinea());
			}
		}

		private Token opAd() throws Exception {
			switch(tokenActual.getNombre()){
			case "Signo +": 
				return match("Signo +"); 
			case "Signo -": 
				return match("Signo -"); 

			default:
				throw new Exception("Error, se esperaba +/' y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
		}
			
		}

		private NodoExpresion expMul() throws Exception{
			System.out.println("t0ken actual es "+ tokenActual.getNombre());

			NodoExpresion n = expUn();
			
			System.out.println("token actual es EN EXMUP"+ tokenActual.getNombre());
			System.out.println("nodo actual es EN expMUL "+ n.getToken().getNombre());
			
			return EU(n);
		}
		
		private NodoExpresion EU(NodoExpresion n) throws Exception {
			List<String> siguientes = new ArrayList<String>();
			
			System.out.println("token actual e EU s "+ tokenActual.getNombre());
			System.out.println("nodo actual de EU es: " + n.getToken().getNombre());
			
			if (tokenActual.getNombre().equals("Signo *")	||
				tokenActual.getNombre().equals("Signo /")) {
					Token t= opMul();
					NodoExpresion neu = expUn();
					NodoExpresionBin neb= new NodoExpresionBin(t, n, neu);
					return EU(neb);
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
				
				if(siguientes.contains(tokenActual.getNombre()))
					return n;
				else
					throw new Exception("Error en la declaracion de EU proveniente de expMul. Expresion erronea en linea "+ tokenActual.getNroLinea());
			}
			
		}

		private NodoExpresion expUn() throws Exception{		
			
			if (tokenActual.getNombre().equals("Signo +") ||
				tokenActual.getNombre().equals("Signo -") ||
				tokenActual.getNombre().equals("Not"))	{
					Token t= opUn();
					NodoExpresion nu= expUn();
					return new NodoExpresionUn(t, nu);
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
					tokenActual.getNombre().equals("Caracter")){
				
					NodoExpresion o= Operando();
					System.out.println("exp un devuelve "+ o.getToken().getNombre());
						return o;
				}
				else
					throw new Exception("Error en la declaracion de expUn. Expresion erronea en linea "+ tokenActual.getNroLinea());
			}
		}
		
		private Token opMul() throws Exception{
			switch(tokenActual.getNombre()){
				case "Signo *": 
					return match("Signo *"); 
				case "Signo /": 
					return match("Signo /"); 

			default:
				throw new Exception("Error, se esperaba * / y se encontro "+tokenActual.getNombre()+" en la linea " + tokenActual.getNroLinea());
			}
		}

		private NodoOperando Operando() throws Exception { 
			System.out.println("entra A OPERANDO ");
			if( tokenActual.getNombre().equals("null") 			||
				tokenActual.getNombre().equals("true") 	  		||
				tokenActual.getNombre().equals("false") 	  	||
				tokenActual.getNombre().equals("Entero") 		||
				tokenActual.getNombre().equals("Caracter") 		||
				tokenActual.getNombre().equals("String")){
				
					Token t= Literal();
					NodoOperandoLiteral nop= new NodoOperandoLiteral(t);
					nop.setToken(t);
					return nop;
					
			}
			else{
				if (tokenActual.getNombre().equals("Parentesis abre") 	||
					tokenActual.getNombre().equals("Id de clase") 		||
					tokenActual.getNombre().equals("new")				||
					tokenActual.getNombre().equals("this")				||
					tokenActual.getNombre().equals("Id Metodo-Variable")) {

						return Primario();
				}
					else
						throw new Exception("Error, se esperaba null/ true / false/ entero/ caracter/ string/"
								+ " ( / this / idMetVar / idClase / new, "
											+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			}
		}
		
		private Token Literal() throws Exception{
			return match(tokenActual.getNombre());
		}

		private Token opUn() throws Exception {
			switch(tokenActual.getNombre()){
				case "Signo +": 
					return match("Signo +");
				case "Signo -": 
					return match("Signo -");
				case "Not":		
					return match("Not");
			default:
				throw new Exception("Error, se esperaba + - ! y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea());
			
			}
		}
		
		private NodoOperandoPrimario Primario() throws Exception{
			switch(tokenActual.getNombre()){
				case "Parentesis abre":
					System.out.println("entra");
					match("Parentesis abre");
					NodoExpresion ne= Expresion();
					NodoExpresionParentizada NEP = new NodoExpresionParentizada(tokenActual, null, ne);
					match("Parentesis cierra");			
					Encadenados(NEP);
					return NEP;
				case "this":
					Token t=match("this");
					NodoAcceso na= new NodoThis(t, null, tabla.getClaseActual(), tabla.getClaseActual().getUnidadActual(), tabla);
					Encadenados(na);
					return na;
				case "Id Metodo-Variable":
					Token tok= match("Id Metodo-Variable");								
					return IMV(tok);
				case "Id de clase":
					Token tk= match("Id de clase");
					match("Punto");
					NodoLlamadaEstatica NLE= new NodoLlamadaEstatica(tk, tokenActual, tabla, tabla.getClaseActual().getNombre(), null);
					llamadaMetodo(NLE);
					Encadenados(NLE);
					return NLE;
				case "new":
					match("new");
					return N();
				default:
					return null;
			}
			
		}

		private NodoConstructor N() throws Exception { 
			if(tokenActual.getNombre().equals("Id de clase")){
				Token t= match("Id de clase");
				NodoCtorComun n= new NodoCtorComun(t, null, t.getNombre(), tabla);
				LinkedList<NodoExpresion> lista= argsActuales();
				n.setArgumentos(lista);
				Encadenados(n);
				return n;
			}
			else 
				if (tokenActual.getNombre().equals("boolean") ||
					tokenActual.getNombre().equals("char")    ||
					tokenActual.getNombre().equals("int")){
						Token tok= match(tokenActual.getNombre());
						match("Corchete abre");
						NodoExpresion e=Expresion();
						match("Corchete cierra");
						NodoCtorArreglo n= new NodoCtorArreglo(tok, null, tok.getNombre(), e);
						Encadenados(n);
						return n;
			}
			else 
				throw new Exception("Error, "
						+ "se esperaba idClase/ boolean/ char/int "
						+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
				
		}

		private LinkedList<NodoExpresion> argsActuales() throws Exception {
			System.out.println("entra a ARGS ACTUALES");

			match("Parentesis abre");
			LinkedList<NodoExpresion> lista= ListaExpsAus();
			match("Parentesis cierra");
			return lista;
		}
		
		private LinkedList<NodoExpresion> ListaExpsAus() throws Exception {
			System.out.println("entra A LISTA EXPS AUS");
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
				tokenActual.getNombre().equals("this")){
				LinkedList<NodoExpresion> lista= new LinkedList<NodoExpresion>();
				listaExps(lista);
				return lista;
			}
			else 
				if (!tokenActual.getNombre().equals("Parentesis cierra")){
					throw new Exception("Error,"
							+ "se esperaba + - ! ( idClase idMetVar new entero caracter string false null true this "
							+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			}
			return new LinkedList<NodoExpresion>();
		}

		private void listaExps(LinkedList<NodoExpresion> lista) throws Exception {
			System.out.println("entra A LISTA EXPS");

			NodoExpresion e= Expresion();

			lista.add(e);
			LE(lista);
		}

		private void LE(LinkedList<NodoExpresion> lista) throws Exception {
			System.out.println("entra A LE");

			if(tokenActual.getNombre().equals("Coma")){
				match("Coma");
				listaExps(lista);
			}
			else 
				if(!tokenActual.getNombre().equals(("Parentesis cierra"))){
					throw new Exception("Error,"
							+ "se esperaba , o ) "
							+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
			}
		}

		private void llamadaMetodo(NodoLlamadaEstatica n) throws Exception {
			Token t= match("Id Metodo-Variable");
			LinkedList<NodoExpresion> argumentos=  argsActuales();
			n.setArgumentos(argumentos);
			n.setTokenLlamada(t);
			Encadenados(n); 
		}

		private NodoOperandoPrimario IMV(Token t) throws Exception {
			System.out.println("entra A IMV ");
			/*	
			if(tokenActual.getNombre().equals("Corchete abre")){
				match("Corchete abre");
				NodoExpresion e= Expresion();
				NodoEncadenadoArreglo NA= new NodoEncadenadoArreglo(t, e, tabla, null);
				Encadenados(NA);
				match("Corchete cierra");
				return NA;
			}
			else*/ if(tokenActual.getNombre().equals("Parentesis abre")){	
				System.out.println("entra AL IF DE IMV ");
				NodoLlamadaDirecta NL = new NodoLlamadaDirecta(t, null, tabla.getClaseActual());
				//NodoEncadenadoLlamada NL= new NodoEncadenadoLlamada(t, tabla, null);
				LinkedList<NodoExpresion> lista = argsActuales();
				NL.setArgumentos(lista);
				Encadenados(NL);
				return NL;
			}
			else{
				NodoVar nv= new NodoVar(t, null, tabla.getBloqueActual());
				Encadenados(nv);
				return nv;
			}
			
		}
		
		private NodoOperandoPrimario IMV2(Token t) throws Exception {
			System.out.println("entra A IMV ");
			/*	
			if(tokenActual.getNombre().equals("Corchete abre")){
				match("Corchete abre");
				NodoExpresion e= Expresion();
				NodoEncadenadoArreglo NA= new NodoEncadenadoArreglo(t, e, tabla, null);
				Encadenados(NA);
				match("Corchete cierra");
				return NA;
			}
			else */if(tokenActual.getNombre().equals("Parentesis abre")){	
				System.out.println("entra AL IF DE IMV ");
				//NodoLlamadaDirecta NL = new NodoLlamadaDirecta(t, null, tabla.getClaseActual());
				NodoEncadenadoLlamada NL= new NodoEncadenadoLlamada(t, tabla, null);
				LinkedList<NodoExpresion> lista = argsActuales();
				NL.setArgumentos(lista);
				Encadenados(NL);
				return NL;
			}
			else{
				NodoEncadenadoIdMetVAr nv= new NodoEncadenadoIdMetVAr(t, tabla, null);
				Encadenados(nv);
				return nv;
			}
			
		}


		private void Encadenados(NodoOperandoPrimario n) throws Exception {

			if(tokenActual.getNombre().equals("Punto")){
				NodoOperandoPrimario ne= Encadenado();
				n.encadenar(ne);
				Encadenados(n);
			}
			else 
				if (tokenActual.getNombre().equals("Corchete abre")){
					NodoOperandoPrimario ne= Encadenado();
					n.encadenar(ne);
					Encadenados(n);
				}
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

		private NodoOperandoPrimario Encadenado() throws Exception{
			if(tokenActual.getNombre().equals("Punto")){
				match("Punto");
				Token t= match("Id Metodo-Variable"); 
				return IMV2(t);
			}
			else 
				if (tokenActual.getNombre().equals("Corchete abre")) {
					match("Corchete abre");
					NodoExpresion e= Expresion(); 
					match("Corchete cierra");

					NodoEncadenadoArreglo nea= new NodoEncadenadoArreglo(tokenActual,e , tabla, null);
					Encadenados(nea);
					return nea;
				}
				else 
					throw new Exception("Error,"
							+ " se esperaba . [ "
							+ "y se encontro "+tokenActual.getNombre()+" en la linea "+ tokenActual.getNroLinea() );
		}
	}