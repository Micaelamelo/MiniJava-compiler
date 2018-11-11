
public class NodoIfElse extends NodoSentencia{
	
	//atributo nodo condicion y atributo 2 sentencias
	
	private NodoExpresion expresion; //condicion
	private NodoSentencia sentencia;
	private NodoSentencia sentenciaElse;
	
	public NodoIfElse(NodoExpresion e, NodoSentencia s, NodoSentencia se, Token t){
		super(t);
		setExpresion(e);
		setSentencia(s);
		setSentenciaElse(se);
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}

	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	public NodoSentencia getSentencia() {
		return sentencia;
	}

	public void setSentencia(NodoSentencia sentencia) {
		this.sentencia = sentencia;
	}

	public NodoSentencia getSentenciaElse() {
		return sentenciaElse;
	}

	public void setSentenciaElse(NodoSentencia sentenciaElse) {
		this.sentenciaElse = sentenciaElse;
	}
	
	public void chequear() throws Exception{
		TipoMetodo t= expresion.chequear();
		
		if(t.getTipo().equals("boolean")){
			if(sentencia != null)
				sentencia.chequear();
			if(sentenciaElse !=null)
				sentencia.chequear();
		}
		else
			throw new Exception("Error, la expresion del if debe ser de tipo boolean. Linea "+ token.getNroLinea());
			
	}
}
