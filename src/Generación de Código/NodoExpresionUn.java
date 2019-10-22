
public class NodoExpresionUn extends NodoExpresion {

	 //nodo expresion q dsp se crea como expresion unaria y dsp n oerador
	private NodoExpresion expresion;
	private NodoOperando operador; //creo q con lo de stirng
	private TablaDeSimbolos ts;
	
	public NodoExpresionUn(Token t, NodoExpresion e, TablaDeSimbolos ta){
		super(t);
		setExpresion(e);
		ts=ta;
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
		
	//	System.out.println("entra aca");

		
		if (operador.equals("+") || operador.equals("-")){
			if (t.getTipo().equals("int"))
				return t;
			else 
				throw new Exception("Error en expresion unaria, la expresion debe ser de tipo entero. Linea "+token.getNroLinea());
		}
		else
			if (operador.equals("!"))
				if (t.getTipo().equals("boolean"))
					return t;
				else 
					throw new Exception("Error en expresion unaria, la expresion debe ser de tipo booleana. Linea "+token.getNroLinea());			
		return t;
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		String operador=token.getLexema();
		
		expresion.generar(t);
		
		switch(operador){
			case "-":  
				t.generarInstruccion("NEG");
				break;		
			case "!": 
				t.generarInstruccion("NOT");	
				break;
		}
		
	}

}
