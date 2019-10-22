
public class NodoEncadenadoIdMetVAr extends NodoEncadenado{

	private boolean esIzq;
	private Atributo a;
	
	public NodoEncadenadoIdMetVAr(Token t, TablaDeSimbolos tt, NodoEncadenado en){
		super(t, tt, en);
		a=null;
		esIzq=false;
	}

	
	public TipoMetodo chequear(TipoMetodo t) throws Exception{
		
	
		Clase c = ts.getClase(t.getTipo());
				
			if (c != null) {
				
				Atributo m = c.getAtributo(token.getLexema());
				
				
				if (m != null && m.getVisibilidad().equals("public")) { 
					Tipo tipoM = m.getTipo();
					a=m;
					
					if (this.getEncadenado() != null) {
						//System.out.println(tipoM.getTipo());
						if (tipoM instanceof TIdClase || tipoM instanceof TString || tipoM instanceof TAInt || tipoM instanceof TABoolean || tipoM instanceof TAChar)
						
							return this.getEncadenado().chequear(tipoM);
					 	else {
								throw new Exception("Error en llamada encadenada. No se puede llamar a un atributo de una clase primitiva. Linea "+token.getNroLinea());
							}
					}
					else{

						return tipoM;
					}
				}
				else {
					throw new Exception("Error en llamada encadenada. El atributo '"+token.getLexema()+"' no existe en la clase "+t.getTipo()+". Linea "+token.getNroLinea());
				}		
			}
			else {
				throw new Exception("Error en llamada encadenada. No se puede llamar a atributo/metodo de tipo primitivo. Linea "+token.getNroLinea());
				}
			}
	
	

	
	public void setIzq() {
		if (this.getEncadenado() == null)
			esIzq = true;
		else
			this.getEncadenado().setIzq();
	}

	@Override
	public Tipo chequear() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generar(TablaDeSimbolos t) {
	
	//	System.out.println("offset a llamar: "+a.getOffset());
		if (this.getEncadenado() != null || esIzq==false){
			t.generarInstruccion("LOADREF "+a.getOffset());
			
		}
		else {
			t.generarInstruccion("SWAP");
			t.generarInstruccion("STOREREF "+a.getOffset());
		}
		
		if (this.getEncadenado() != null) 
			this.getEncadenado().generar(t);	
		
	
	}
}
