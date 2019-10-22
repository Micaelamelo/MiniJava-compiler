
public class NodoEncadenadoArreglo extends NodoEncadenado{
//nodoexpresion de adentro de los corchetes
	
	private NodoExpresion expresion;
	private boolean esIzq;
	
	public NodoEncadenadoArreglo(Token t, NodoExpresion e, TablaDeSimbolos tt, NodoEncadenado en){
		super(t,tt,en);
		expresion=e;
		esIzq=false;
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}

	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}
	
	
	public TipoMetodo chequear(TipoMetodo t) throws Exception{  
			//	if(encadenado ==null){
					if(t instanceof TABoolean)
						return new TBoolean("boolean");
					else if (t instanceof TAInt)
						return new TInt("int");
					else if (t instanceof TAChar)
						return new TChar("char");
					else
						throw new Exception("El arreglo debe ser de tipo boolean, char o int. Linea "+token.getNroLinea());
			//	}
			//	else 
			//		throw new Exception("El arreglo no puede ser encadenado");
	}

	@Override
	public Tipo chequear() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIzq() {
		if (this.getEncadenado() == null)
			esIzq = true;
		else
			this.getEncadenado().setIzq();
		
	}
	
	@Override
	public void generar(TablaDeSimbolos t) {
		
		expresion.generar(t);
		t.generarInstruccion("ADD");
		if(esIzq==true && getEncadenado()==null){
			t.generarInstruccion("SWAP");
			t.generarInstruccion("STOREREF 0");
		}
		else 
			t.generarInstruccion("LOADREF 0");
	}	

}
