
import java.util.LinkedList;

public class NodoDecVars extends NodoSentencia{
	
	private LinkedList<Atributo> variables;
	private NodoBloque bloque;
	private TablaDeSimbolos tabla;
	
	public NodoDecVars(NodoBloque b, Token t, TablaDeSimbolos ts){
		super(t);
		bloque=b;
		setTabla(ts);
		variables=new LinkedList<Atributo>();
	}
	
	public void chequear() throws Exception{
	
		for(Atributo e : variables) {		
			Tipo t = e.getTipo();		
			if (t instanceof TIdClase){
				if (tabla.existeClase(t.getTipo())) {
					bloque.insertarVariableLocal(e);
				}
				else
					throw new Exception("Error, el tipo de la variable '"+e.getNombre()+"' es de una clase que no existe. Linea "+e.getNroLinea());
			}
			else 
				bloque.insertarVariableLocal(e);				
		}
	}
	
	public void insertarVariable(Atributo e) {
		variables.addLast(e);		
	}

	public TablaDeSimbolos getTabla() {
		return tabla;
	}

	public void setTabla(TablaDeSimbolos tabla) {
		this.tabla = tabla;
	}

	public LinkedList<Atributo> getVariables() {
		return variables;
	}

	public void setVariables(LinkedList<Atributo> variables) {
		this.variables = variables;
	}

	public NodoBloque getBloque() {
		return bloque;
	}

	public void setBloque(NodoBloque bloque) {
		this.bloque = bloque;
	}

	@Override
	public void generar(TablaDeSimbolos t) {	
		t.generarInstruccion("RMEM "+variables.size());
	}
	
}
