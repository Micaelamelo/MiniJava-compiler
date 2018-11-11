
public class NodoWhile extends NodoSentencia{
//nodo expresion
//nodo sentencia: cuerpo del while
	
	private NodoExpresion expresion;
	private NodoSentencia sentencia;
	
	public NodoWhile(NodoExpresion nE, NodoSentencia nS, Token t) {
		super(t);
		setExpresion(nE);
		setSentencia(nS);
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
	
	public void chequear() throws Exception{
		TipoMetodo t= expresion.chequear();
		
		if(t.getTipo().equals("boolean"))
			sentencia.chequear();
		else
			throw new Exception("Error, la expresion dentro del while debe ser de tipo booleana. Linea "+token.getNroLinea());
	}
	
}
