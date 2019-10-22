
public class NodoCtorArreglo extends NodoConstructor{

	private NodoExpresion tamanio;
	
	public NodoCtorArreglo(Token t, NodoEncadenado e, String n, NodoExpresion ex) {
		super(t, e, n);
		tamanio=ex;
	}

	@Override
	public TipoMetodo chequear() throws Exception {
		TipoMetodo tam= tamanio.chequear();
		TipoMetodo ret=null;
		if(tam instanceof TInt) {
			switch(token.getLexema()) {
			case "boolean":
				ret=new TABoolean("TAboolean");
				break;
			case "char":
				ret=new TAChar("TAchar");
				break;
			case "int":
				ret=new TAInt("TAint");
				break;
			}
		}
			else
				throw new Exception("El arreglo debe contener una expresion numeria entre []. Linea "+token.getNroLinea());
		return ret;
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void generar(TablaDeSimbolos t) {	
		t.generarInstruccion("RMEM 1");
		tamanio.generar(t);
		t.generarInstruccion("PUSH simple_malloc");
		t.generarInstruccion("CALL");
	}
	
	
}
