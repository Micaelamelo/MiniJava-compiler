
public class NodoOperandoLiteral extends NodoOperando{

	//token, puede ser null
		private Token token;
		
		public NodoOperandoLiteral(Token t){
			super(t);
		}

		public Token getToken() {
			return token;
		}

		public void setToken(Token token) {
			this.token = token;
		}
		
		public TipoMetodo chequear() throws Exception{
			String tipo = token.getNombre();
			Tipo t = null;
			
				switch(tipo){
					case "Entero":  	t= new TInt("int"); 		break;
					case "true": 		t= new TBoolean("boolean");	break;
					case "false": 		t= new TBoolean("boolean");	break;
					case "Caracter": 	t= new TChar("char");		break;
					case "String":  	t= new TString("String");	break;
					case "null": 		t= new TNull("null");		break;
				}
				return t;
		}

		@Override
		public void generar(TablaDeSimbolos t) {
			String cadena= this.token.getLexema();
			switch(token.getNombre()){
				case "false":
					t.generarInstruccion("PUSH 0"); 
					break;
				case "null": 
					t.generarInstruccion("PUSH 0"); 
					break;
 				case "true":
 					t.generarInstruccion("PUSH 1");
 					break; 
				case "Entero": 
					t.generarInstruccion("PUSH " + this.token.getLexema()); 
					break;
				case "Caracter": 
					char letra = '0';
					if (token.getLexema().length()==1)
						letra=token.getLexema().charAt(0);
					t.generarInstruccion("PUSH "+(int) letra); 
					break;
				case "String":
					String label  = t.getEtiqueta();				
					t.generarInstruccion(".DATA");
					t.generarInstruccion(label+ ": DW \""+ cadena+"\",0");							
					t.generarInstruccion(".CODE");
					t.generarInstruccion("PUSH "+  label);
					break;
			}
			
		}
		
		
}
