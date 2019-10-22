
public class NodoExpresionBin extends NodoExpresion{

	//nodo expresion der, izq y operador
	private NodoExpresion ladoIzquierdo;
	private NodoExpresion ladoDerecho;
	private TablaDeSimbolos ts;
//	private NodoOperando operador;
	
	public NodoExpresionBin(Token t, NodoExpresion Li, NodoExpresion Ld, TablaDeSimbolos ta){
		super(t);
		setLadoIzquierdo(Li);
		setLadoDerecho(Ld);
		ts=ta;
	//	setOperador(o);
	}

	public NodoExpresion getLadoIzquierdo() {
		return ladoIzquierdo;
	}

	public void setLadoIzquierdo(NodoExpresion ladoIzquierdo) {
		this.ladoIzquierdo = ladoIzquierdo;
	}

	public NodoExpresion getLadoDerecho() {
		return ladoDerecho;
	}

	public void setLadoDerecho(NodoExpresion ladoDerecho) {
		this.ladoDerecho = ladoDerecho;
	}

	public TipoMetodo chequear() throws Exception{
		TipoMetodo LI = ladoIzquierdo.chequear();
		TipoMetodo LD = ladoDerecho.chequear();

		String operador = token.getLexema();
		
		if (operador.equals("+") || operador.equals("-") || operador.equals("*") || operador.equals("/"))
			if (LI.getTipo().equals("int") && LD.getTipo().equals("int"))
				return LI;
			else 
				throw new Exception("Error en expresion binaria "+operador+", ambas subexpresiones deben ser de tipo entero. Linea "+token.getNroLinea());
		else
			if (operador.equals("&&") || operador.equals("||"))
				if (LI.getTipo().equals("boolean") && LD.getTipo().equals("boolean"))
					return LI;
				else 
					throw new Exception("Error en expresion binaria "+operador+", ambas subexpresiones deben ser de tipo booleano. Linea "+token.getNroLinea());
			else
				if (operador.equals("==") || operador.equals("!="))
					if (LI.conformidad(LD) || LD.conformidad(LI))
						return new TBoolean("boolean");
					else 
						throw new Exception("Error en expresion igualdad, ambas subexpresiones deben ser del mismo tipo. Linea "+token.getNroLinea());
				else
					if (operador.equals(">") || operador.equals("<") || operador.equals(">=") || operador.equals("<="))
						if (LI.getTipo().equals("int") && LD.getTipo().equals("int"))
							return new TBoolean("boolean");
						else 
							throw new Exception("Error en expresion comparacion, ambas subexpresiones deben ser de tipo entero. Linea "+token.getNroLinea());
		return null;				
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		ladoIzquierdo.generar(t);
		ladoDerecho.generar(t);
		
		String op = token.getLexema();
		
		switch(op){
			case "+":
				t.generarInstruccion("ADD");
				break;
			case "-":
				t.generarInstruccion("SUB");
				break;
			case "*":
				t.generarInstruccion("MUL");
				break;
			case "/":
				t.generarInstruccion("DIV");
				break;
			case "&&":
				t.generarInstruccion("AND");
				break;
			case "||":
				t.generarInstruccion("OR");
				break;
			case "==": 
				t.generarInstruccion("EQ");
				break;
			case "!=":
				t.generarInstruccion("NE");
				break;
			case "<":
				t.generarInstruccion("LT");
				break;
			case ">":
				t.generarInstruccion("GT");
				break;
			case "<=":
				t.generarInstruccion("LE");
				break;
			case ">=" :
				t.generarInstruccion("GE");
				break;
			
		}
		
	}
	
}
