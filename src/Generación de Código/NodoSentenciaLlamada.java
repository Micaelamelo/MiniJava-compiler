
public class NodoSentenciaLlamada extends NodoSentencia{
	
	private NodoExpresion sentencia;
	private TipoMetodo tipo;
	
	public NodoSentenciaLlamada(Token t, NodoExpresion np){
		super(t);
		sentencia=np;
		tipo=null;
	}
	
	public void chequear() throws Exception{
		tipo= sentencia.chequear();
	}
	
	public void setSentencia(NodoExpresion np){
		sentencia= np;
	}
	
	public void setToken(Token t){
		token=t;
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		sentencia.generar(t);
		if (!tipo.getTipo().equals("void"))
			t.generarInstruccion("POP");
		
	}
}
