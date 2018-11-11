import java.util.LinkedList;

public abstract class NodoOperandoPrimario extends NodoOperando{

	protected NodoOperandoPrimario encadenado;
	
	public NodoOperandoPrimario(Token t, NodoEncadenado e){
		super(t);
		encadenado=e;
	}
	
	abstract public TipoMetodo chequear(TipoMetodo t) throws Exception;
	
	public void encadenar(NodoOperandoPrimario nE) {
		if (encadenado == null)
			encadenado = nE;	
		else
			encadenado.encadenar(nE);
	}

}
