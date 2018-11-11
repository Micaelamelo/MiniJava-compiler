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
	
	public void chequearBloque() throws Exception{
		if (bloque != null)
			bloque.chequear();
		if (!tieneReturn && !tipo.getTipo().equals("void")) 
			throw new Exception("Error, el metodo "+nombre+" no tiene retorno ");
	}
	
	public boolean equalsM(Metodo m) throws Exception{

		if(m.getParametros().size()==parametros.size()){
			if(m.getTipo().getTipo().equals(tipo.getTipo()) && m.getFormaMetodo().equals(formaMetodo)){
				for(int i=0; i<parametros.size(); i++){
					if(!parametros.get(i).getTipo().getTipo().equals(m.getParametros().get(i).getTipo().getTipo()))
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
	
}
