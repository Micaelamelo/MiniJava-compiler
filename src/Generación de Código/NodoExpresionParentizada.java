
public class NodoExpresionParentizada extends NodoOperandoPrimario {

	private NodoExpresion expresion;
	
	public NodoExpresionParentizada(Token t, NodoEncadenado ne, NodoExpresion nex){
		super(t,ne);
		expresion=nex;
	}
	
	public TipoMetodo chequear() throws Exception{
		TipoMetodo t= expresion.chequear();
		
		if(encadenado!=null)
			return encadenado.chequear(t);
		else
			return t;
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		expresion.generar(t);		
		if(encadenado != null)
			encadenado.generar(t);			
	}
	
	
}
