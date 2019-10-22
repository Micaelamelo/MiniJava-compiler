import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Unidad {
	
	protected TablaDeSimbolos tablaSimb;
	protected ArrayList<Parametro> parametros;
	protected Map<String, Parametro> Tparametros;
	protected NodoBloque bloque;
	protected int nroLinea;
	protected String label;
	protected int offset, offsetVT;
	protected String nombre;
	protected String formaMetodo;
	
	
	public Unidad(String nom, TablaDeSimbolos t, int n){
		nombre=nom;
		bloque=null;
		tablaSimb= t;
		parametros= new ArrayList<Parametro>();
		nroLinea=n;
		label = null;
		formaMetodo=null;
		offset= 0;
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

	abstract public TipoMetodo getTipo();
		
	
	protected boolean existeParametro(String nombre) {
		//Chequea si existe algun parametro con el nombre pasado por parametro
		for(Parametro e : parametros) {
			if(e.getNombre().equals(nombre))
				return true;
		}
		return false;
	}
	
	public String getFormaMetodo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void insertarParametro(Parametro p) throws Exception{
		if(!Tparametros.containsKey(p.getNombre())){
			parametros.add(p);
			Tparametros.put(p.getNombre(), p);
			if(formaMetodo != null && formaMetodo.equals("static")){
			
				p.setOffset(3+offset); //esta bien que sea 3
			}
			else{
				p.setOffset(4+offset);
				//System.out.println("inserte el parametro :" +p.getNombre() + "con offset: "+4+offsetRA);
			}
				offset++;
			
		}
		else
			throw new Exception("El parametro "+p.getNombre()+" en el metodo "+this.getNombre()+ " en la linea "+ nroLinea +" ya existe");
	}
	
	public void chequearBloque() throws Exception{
		if (bloque != null)
				bloque.chequear();
	}
	
	public int getOffsetVT() {
		return offsetVT;
	}
	
	public int getOffsetRA() { //por las dudas otro nombre que no se confunda con el offset de atributo 
		return offset;
	}
	
	public void setOffsetRA(int off) { // idem anterior
		offset = off;
	}

	public void setOffsetVT(int off) {
		offsetVT = off;
	}
	
	public void setLabel(String l) {
		label = l;
	}
	
	public String getLabel() {
		return label;
	}
	
	

}
