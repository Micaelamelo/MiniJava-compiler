import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Unidad {
	
	protected TablaDeSimbolos tablaSimb;
	protected ArrayList<Parametro> parametros;
	protected Map<String, Parametro> Tparametros;
	protected NodoBloque bloque;
	protected int nroLinea;
	
	protected String nombre;
	
	public Unidad(String nom, TablaDeSimbolos t, int n){
		nombre=nom;
		bloque=null;
		tablaSimb= t;
		parametros= new ArrayList<Parametro>();
		nroLinea=n;
		Tparametros= new HashMap<String, Parametro>();
	}
	
	public void setBloque(NodoBloque b){
		bloque=b;
	}
	
	public NodoBloque getBloque(){
		return bloque;
	}
	
	public String getNombre(){
		return nombre;
	}

	public int getNroLinea(){
		return nroLinea;
	}
	
	public ArrayList<Parametro> getParametros(){
		return parametros;
	}
	
	public TablaDeSimbolos getTablaDeSimbolos(){
		return tablaSimb;
	}

	public String getFormaMetodo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void insertarParametro(Parametro p) throws Exception{
		if(!Tparametros.containsKey(p.getNombre())){
			parametros.add(p);
			Tparametros.put(p.getNombre(), p);
		}
		else
			throw new Exception("El parametro "+p.getNombre()+" en el metodo "+this.getNombre()+ " en la linea "+ nroLinea +" ya existe");
	}
	
	public void chequearBloque() throws Exception{
		if (bloque != null)
				bloque.chequear();
	}
	

}
