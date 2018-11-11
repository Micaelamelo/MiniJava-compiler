
public class NodoExpresionUn extends NodoExpresion {

	 //nodo expresion q dsp se crea como expresion unaria y dsp n oerador
	private NodoExpresion expresion;
	private NodoOperando operador; //creo q con lo de stirng
	
	public NodoExpresionUn(Token t, NodoExpresion e){
		super(t);
		setExpresion(e);
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}

	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	public NodoOperando getOperador() {
		return operador;
	}

	public void setOperador(NodoOperando operador) {
		this.operador = operador;
	}
	
	public TipoMetodo chequear() throws Exception{
		TipoMetodo t = expresion.chequear();
		String operador = token.getLexema();
		
	
		if (operador.equals("+") || operador.equals("-"))
			if (t.getTipo().equals("int"))
				return t;
			else 
				throw new Exception("Error en expresion unaria, la expresion debe ser de tipo entero. Linea "+token.getNroLinea());
		else
			if (operador.equals("!"))
				if (t.getTipo().equals("boolean"))
					return t;
				else 
					throw new Exception("Error en expresion unaria, la expresion debe ser de tipo booleana. Linea "+token.getNroLinea());			
		return t;
	}

}
