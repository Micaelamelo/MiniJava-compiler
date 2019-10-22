import java.util.ArrayList;

public class Metodo extends Unidad {
	
	private TipoMetodo tipo;
	private String formaMetodo;
	private boolean tieneReturn;
	
	public Metodo(String nombre, TablaDeSimbolos t, TipoMetodo tip, String fm, int n){
		super(nombre,t,n);
		tipo=tip;
		formaMetodo=fm;
		tieneReturn=false;
	}
	
	public String getFormaMetodo() {
		return formaMetodo;
	}

	public void tieneReturn(){
		tieneReturn=true;
	}
	
	public String getNombre(){
		return super.getNombre();
	}
	
	public ArrayList<Parametro> getParametros(){
		return super.getParametros();
	}
	
	public TipoMetodo getTipo(){
		return tipo;
	}
	
	public void insertarParametro(Parametro e) throws Exception {
		if(!existeParametro(e.getNombre())) {
			parametros.add(e);
			if (formaMetodo.equals("static")) {
				e.setOffset(3+offset);
			}
			else
				e.setOffset(4+offset);
			offset++;
		}
		else
			throw new Exception("Error semantico: Ya existe un parametro con nombre "+e.getNombre()+" en metodo "+nombre+". Linea "+e.getNroLinea());
	}
	
	public void chequearBloque() throws Exception{
		if (bloque != null)
			bloque.chequear();
		if (!tieneReturn && !tipo.getTipo().equals("void")) 
			throw new Exception("Error, el metodo "+nombre+" no tiene retorno ");
	}
	
	public boolean equalsM(Metodo m) throws Exception{

		if(getParametros().size()==parametros.size()){
			if(getTipo().getTipo().equals(tipo.getTipo()) && getFormaMetodo().equals(formaMetodo)){
				for(int i=0; i<parametros.size(); i++){
					if(!parametros.get(i).getTipo().getTipo().equals(getParametros().get(i).getTipo().getTipo()))
						throw new Exception("El metodo "+nombre+" de la linea "+ nroLinea +" no esta redefinido con el orden y/o tipo de los parametros correctos");
				}
			}
			else
				throw new Exception("El metodo "+nombre+" de la linea "+ nroLinea +" no esta redefinido con el tipo de retorno y/o forma metodo correcto ");
		}
		else
			throw new Exception("El metodo "+nombre+" de la linea "+ nroLinea +" no esta redefinido con la cantidad de parametros correcta ");
		
		return true;
	}

	
	public void generar(TablaDeSimbolos t){
		if (getFormaMetodo().equals("static")) {
			tablaSimb.generarInstruccion(".CODE");
			
			if(getBloque()!= null){
				tablaSimb.generarInstruccion(getLabel()+ ": ");
				tablaSimb.generarInstruccion("LOADFP");
				tablaSimb.generarInstruccion("LOADSP");
				tablaSimb.generarInstruccion("STOREFP");
			
				getBloque().generar(tablaSimb);
				
				if(getNombre().equals("main"))
					tablaSimb.generarInstruccion("HALT");
				else {
					tablaSimb.generarInstruccion("STOREFP");
					tablaSimb.generarInstruccion("RET "+(getParametros().size()));
				}
			}
		}
		else {
			tablaSimb.generarInstruccion(".CODE");
			if(getBloque()!= null){
				tablaSimb.generarInstruccion(getLabel()+ ": ");
				tablaSimb.generarInstruccion("LOADFP");
				tablaSimb.generarInstruccion("LOADSP");
				tablaSimb.generarInstruccion("STOREFP");
				getBloque().generar(tablaSimb);
				tablaSimb.generarInstruccion("STOREFP");
				tablaSimb.generarInstruccion("RET "+(getParametros().size()+1));
			}
		}

	}
}
