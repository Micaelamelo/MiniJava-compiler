import java.util.LinkedList;

public class TIdClase extends Tipo{

	private TablaDeSimbolos tabla;
	
	public TIdClase(TablaDeSimbolos t, String nombre) {
		super(nombre);
		tabla=t;
	}
	
	public String getTipo(){
		return super.getTipo();
	}
 
	public boolean esSubTipo(TipoMetodo t) throws Exception {
		if ( !(t instanceof TIdClase)  || t.getTipo().equals("void") || t.getTipo().equals("null")){
			System.out.println("clase es");
			return false;
		}
		else
			if (t.getTipo().equals("Object"))
				return true;
			else if (nombre.equals(t.getTipo()))
				return true;
			else{
				
				String padre;
				
					padre = tabla.getClase(nombre).getHeredaDe();
					
					while(!padre.equals("Object")){
						
						if(padre.equals(t.getTipo()))
							return true;
						
						padre =  tabla.getClase(padre).getHeredaDe();
						
					}
			}
		
		return false;
	}
	
}
