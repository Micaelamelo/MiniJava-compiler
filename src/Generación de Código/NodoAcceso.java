
public abstract class NodoAcceso extends NodoOperandoPrimario{
//token}

	protected boolean esIzq;
	
	public NodoAcceso(Token t, NodoEncadenado ne){
		super(t, ne);
		esIzq=false;
	}

	public void setIzq() {
		esIzq=true;
	}

	
}
