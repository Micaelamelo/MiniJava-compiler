import java.util.ArrayList;
import java.util.LinkedList;

public class NodoCtorComun extends NodoConstructor{

	private LinkedList<NodoExpresion> argumentosActuales;
	private TablaDeSimbolos tabla;
	private Constructor cons;
	
	public NodoCtorComun(Token t, NodoEncadenado e, String n, TablaDeSimbolos ts) {
		super(t, e, n);
		cons=null;
		tabla=ts;
		argumentosActuales= new LinkedList<NodoExpresion>();
	}
	
	public void setArgumentos(LinkedList<NodoExpresion> l){
		argumentosActuales=l;
	}

	
	public TipoMetodo chequear() throws Exception {

		Clase clase = tabla.getClase(token.getLexema());
		
		if (clase != null) {

			Constructor c = clase.getConstructor();		
			cons=c;
			
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

	@Override
	public void generar(TablaDeSimbolos t) {
		
		try {
		t.generarInstruccion("RMEM 1");
		t.generarInstruccion("PUSH "+ ((t.getClase(cons.getNombre()).getAtributosL().size())+1));
		t.generarInstruccion("PUSH simple_malloc");
		t.generarInstruccion("CALL");
		t.generarInstruccion("DUP");	
		
		if (tabla.getClase(cons.getNombre()).cantMetodosDinamicos() > 0) {
			t.generarInstruccion("PUSH VT_"+ cons.getNombre());
			t.generarInstruccion("STOREREF 0");
			t.generarInstruccion("DUP");	
		}		
	
		int j = argumentosActuales.size()-1;		
		while(j >= 0){
			argumentosActuales.get(j).generar(t);
			t.generarInstruccion("SWAP");
			j--;
		}	
		
		t.generarInstruccion("PUSH "+ cons.getLabel());
		t.generarInstruccion("CALL");	
		
		if(this.getEncadenado() != null)
			this.getEncadenado().generar(t);	
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
