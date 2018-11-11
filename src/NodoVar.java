
public class NodoVar extends NodoAcceso{
	
	private NodoBloque b;
	
	public NodoVar(Token t, NodoEncadenado ne, NodoBloque bloque){
		super(t, ne);
		b=bloque;
	}
	
	public TipoMetodo chequear() throws Exception{
		System.out.println("Entra a chequear de nodovar");
		System.out.println("Token actual es: "+token.getLexema());
		
		Atributo a = b.existeVariable(token.getLexema());		
		
		if (a != null) {
			System.out.println("Entra a IF de NodoVAR");
			if (encadenado != null){
				System.out.println("En chequear de nodo var voy a devolver "+a.getTipo());
				return encadenado.chequear(a.getTipo());
			}
			else{
				return a.getTipo();
			}
		}
		else {		
			System.out.println("Entra a ELSE de NodoVAR");
			
			Parametro e = b.existeParametro(token.getLexema());
			
			
			if (e != null) {
				if (encadenado != null) {
					System.out.println("Entra a IF DE PARAMETRO DE NODOVAR");
					return encadenado.chequear(e.getTipo());
				}
				else {
					System.out.println("Entra a ELSE DE PARAMETRO DE NODOVAR");
					return e.getTipo();
				}
			}
			
			
			a = b.existeAtributoInstancia(token);
				if (a != null) {
					if (encadenado != null){
						System.out.println("Llega aca "+a.getTipo().getTipo());
						return encadenado.chequear(a.getTipo());
						}	
					else
						return a.getTipo(); //esto no iria
				}					
				else {
					throw new Exception("Error, el atributo '"+token.getLexema()+"'  no existe en la clase o bloque. Linea "+token.getNroLinea());
				}
		}
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

