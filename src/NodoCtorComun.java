import java.util.ArrayList;
import java.util.LinkedList;

public class NodoCtorComun extends NodoConstructor{

	private LinkedList<NodoExpresion> argumentosActuales;
	private TablaDeSimbolos tabla;
	
	public NodoCtorComun(Token t, NodoEncadenado e, String n, TablaDeSimbolos ts) {
		super(t, e, n);
		tabla=ts;
		argumentosActuales= new LinkedList<NodoExpresion>();
	}
	
	public void setArgumentos(LinkedList<NodoExpresion> l){
		argumentosActuales=l;
	}

	
	public TipoMetodo chequear() throws Exception {

		Clase clase = tabla.getClase(token.getLexema());
	

	/*	if (token.getLexema().equals("Object") || 
			token.getLexema().equals("System"))
			throw new Exception("No puede crearse un objecto Object o System. Linea "+ token.getNroLinea());
	*/
		
		if (clase != null) {
			
			System.out.println("clase es "+ clase.getNombre());
			
			Constructor c = clase.getConstructor();		
			
			System.out.println("constructor es "+ c.getNombre());
			
			if (token.getLexema().equals(c.getNombre())) 
				chequearArgumentos(c.getParametros());
			else
				throw new Exception("Error en llamada a constructor, el nombre es incorrecto. Linea "+token.getNroLinea());
			
			if (encadenado != null)
				return encadenado.chequear(new TIdClase( tabla, clase.getNombre()));
			else{
				Tipo t= new TIdClase( tabla, clase.getNombre());
				return t;
			}
		}
		throw new Exception("Error, la clase '"+token.getLexema()+"' no existe. Linea "+token.getNroLinea());
	}
	
	private boolean chequearArgumentos(ArrayList<Parametro> lista) throws Exception{
		
		//Chequea que los tipos de los parametros sean compatibles con los tipos de los parametros formales del metodo

		if(lista.size() == argumentosActuales.size()){
			int i = 0;
			while(i < lista.size()){
				if(!argumentosActuales.get(i).chequear().conformidad(lista.get(i).getTipo())){
					throw new Exception("Error en llamada a metodo '"+token.getLexema()+"', se espera parametro tipo "+lista.get(i).getTipo().getTipo()+" o subtipo. Linea "+token.getNroLinea());
				}
				i++;
			}
			return true;
		}
		else
			throw new Exception("Error al llamar a "+token.getLexema()+", tienen distinta cantidad de parametros. Linea "+token.getNroLinea());
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
