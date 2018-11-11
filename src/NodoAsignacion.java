
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
		
		System.out.println("Entra a chequear de asignacion");
		
		if(ladoIzquierdo instanceof NodoThis)
			ladoIzquierdo.setIzq();
		
		TipoMetodo LI = ladoIzquierdo.chequear();
		System.out.println(" y el lado izquierdo es :"+LI.getTipo());

		
		System.out.println("El lado derecho es :"+ ladoDerecho.toString().toString());
		TipoMetodo LD = ladoDerecho.chequear();
		System.out.println("El lado derecho es :"+ LD.getTipo());

		
		if (!LD.conformidad(LI)){
			throw new Exception("El lado derecho de la asignacion debe ser de tipo '"+LI.getTipo()+"' o heredear del mismo. En la "
					+ "linea "+ token.getNroLinea());
		}
		
		System.out.println("OK ASIGNACION");
	}
	
	
}
