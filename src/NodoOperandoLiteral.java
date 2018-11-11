
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
		
		
}
