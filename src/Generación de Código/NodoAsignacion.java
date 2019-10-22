
public class NodoAsignacion extends NodoSentencia {

	
//nodo exp lado derecho
//nodoacceso q puede ser nodo variable o nodo this
	
	private NodoAcceso ladoIzquierdo;
	private NodoExpresion ladoDerecho;
	
	public NodoAsignacion(NodoAcceso id, NodoExpresion e, Token t) {
		super(t);
		ladoIzquierdo = id;
		ladoDerecho = e;
	}
	
	public NodoAcceso getLadoIzquierdo() {
		return ladoIzquierdo;
	}
	
	public void setLadoIzquierdo(NodoAcceso ladoIzquierdo) {
		this.ladoIzquierdo = ladoIzquierdo;
	}
	
	public NodoExpresion getLadoDerecho() {
		return ladoDerecho;
	}
	
	public void setLadoDerecho(NodoExpresion ladoDerecho) {
		this.ladoDerecho = ladoDerecho;
	}
	
	public void chequear() throws Exception{
		
	//	if (ladoIzquierdo.getEncadenado() instanceof NodoEncadenadoLlamada && ladoIzquierdo.getEncadenado().getEncadenado()==null)
	//		throw new  Exception("No es posible asignar un valor a una llamada a metodo. Linea " +ladoIzquierdo.getToken().getNroLinea());

	
		
		if(ladoIzquierdo.getEncadenado()==null)
			ladoIzquierdo.setIzq();
		else
			ladoIzquierdo.getEncadenado().setIzq();
		
		TipoMetodo LI = ladoIzquierdo.chequear();

		TipoMetodo LD = ladoDerecho.chequear();


		
		if (!LD.conformidad(LI)){
			throw new Exception("El lado derecho de la asignacion debe ser de tipo '"+LI.getTipo()+"' o heredear del mismo. En la "
					+ "linea "+ token.getNroLinea());
		}
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		ladoDerecho.generar(t);
		ladoIzquierdo.generar(t);	
	}
	
	
}
