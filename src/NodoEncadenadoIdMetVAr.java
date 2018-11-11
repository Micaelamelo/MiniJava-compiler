
public class NodoEncadenadoIdMetVAr extends NodoEncadenado{

	public NodoEncadenadoIdMetVAr(Token t, TablaDeSimbolos tt, NodoEncadenado en){
		super(t, tt, en);
	}
	
	public TipoMetodo chequear(TipoMetodo t) throws Exception{
		
		System.out.println("entro a encadenado de nodo id met var, quiero ver si existe el atributo de tipo " + t.getTipo());
		
		Clase c = ts.getClase(t.getTipo());
				
			if (c != null) {
				
				System.out.println("El atributo que quiero obtener es: "+ token.getLexema());
				Atributo m = c.getAtributo(token.getLexema());
				
				System.out.println("El atributo es "+ m.getNombre());
				
				if (m != null && m.getVisibilidad().equals("public")) { 
					Tipo tipoM = m.getTipo();
					if (encadenado != null) {
						if (tipoM instanceof TIdClase || tipoM instanceof TString)
							return encadenado.chequear(tipoM);
						else {
							throw new Exception("Error en llamada encadenada. No se puede llamar a un atributo de una clase primitiva. Linea "+token.getNroLinea());
						}
					}
					else
						return tipoM;	
				}
				else {
					throw new Exception("Error en llamada encadenada. El atributo '"+token.getLexema()+"' no existe en la clase "+t.getTipo()+". Linea "+token.getNroLinea());
				}		
			}
			else {
				throw new Exception("Error en llamada encadenada. No se puede llamar a atributo/metodo de tipo primitivo. Linea "+token.getNroLinea());
				}
			}

	@Override
	public Tipo chequear() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
