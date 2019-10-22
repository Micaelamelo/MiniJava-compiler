
public abstract class NodoEncadenado extends NodoOperandoPrimario{

	
	protected NodoEncadenado encadenado;
	protected Token token;
	protected TablaDeSimbolos ts;
	
	public NodoEncadenado(Token t, TablaDeSimbolos tt, NodoEncadenado e){
		super(t,e);
		encadenado=null;
		token=t;
		ts=tt;
	}
	
	public abstract void setIzq();
	
	public void encadenar(NodoEncadenado nE) {	
		if(encadenado == null)
			encadenado = nE;
		else
			encadenado.encadenar(nE);
	}
}
