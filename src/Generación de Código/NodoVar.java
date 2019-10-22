
public class NodoVar extends NodoAcceso{
	
	private NodoBloque b;
	private int offset;
	private boolean esInst;
	
	public NodoVar(Token t, NodoEncadenado ne, NodoBloque bloque){
		super(t, ne);
		b=bloque;
		esIzq=false;
		esInst=false;
		offset=0;
	}
	
	public TipoMetodo chequear() throws Exception{
	
		Atributo a = b.existeVariable(token.getLexema());		
		
		if (a != null) {
			offset=a.getOffset();
			
			if (encadenado != null){
				
				return encadenado.chequear(a.getTipo());
			}
			else{
				return a.getTipo();
			}
		}
		else {		
			Parametro e = b.existeParametro(token.getLexema());
			
			if (e != null) {
				offset=e.getOffset();
				if (encadenado != null) {

					return encadenado.chequear(e.getTipo());
				}
				else {
					return e.getTipo();
				}
			}
			
			else {
				a = b.existeAtributoInstancia(token);
					if (a != null) {
	
						offset=a.getOffset();
						esInst=true;
						if (encadenado != null){
							return encadenado.chequear(a.getTipo());
							}	
						else
							return a.getTipo(); 
					}					
					else {
						throw new Exception("Error, el atributo '"+token.getLexema()+"'  no existe en la clase o bloque. Linea "+token.getNroLinea());
					}
				}
		}
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		//System.out.println(" lexema "+token.getLexema()+" nro linea "+token.getNroLinea()+" offset "+offset);
		
		if (esInst) {			
			t.generarInstruccion("LOAD 3");			
			if (!esIzq || encadenado != null)
				t.generarInstruccion("LOADREF "+offset);
			else {
				t.generarInstruccion("SWAP");
				t.generarInstruccion("STOREREF "+offset);
			}			
		}
		else {
			if (!esIzq || encadenado != null)
				t.generarInstruccion("LOAD "+offset);
			else 
				t.generarInstruccion("STORE "+offset);
		}				
		
		if (this.getEncadenado() != null)
			this.getEncadenado().generar(t);
		
	}
}

