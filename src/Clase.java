import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Clase {
	
	private String nombre;
	private String heredaDe; //padre
	
	private Unidad unidadActual; //la unidad actual puede ser constructor o metodo
	private Constructor constructor; //SIEMPRE tendra solo UN constructor
	private TablaDeSimbolos tablaSimb;
	
	private boolean tieneMain;
	private boolean consolidada;
	
	private int nroLinea;
		
	protected Map<String, Metodo> Tmetodos;
	protected Map<String, Atributo> Tatributos;
	
	public Clase(String nom, String her,TablaDeSimbolos ts, int n){
		nombre=nom;
		heredaDe=her;
		unidadActual=null;
		constructor=null;
		tieneMain=false;
		tablaSimb=ts;
		
		consolidada=false;
		
		Tmetodos= new HashMap<String, Metodo>();
		Tatributos= new HashMap<String, Atributo>();
		
		nroLinea=n;
	}

	public String getNombre() {
		return nombre;
	}

	public String getHeredaDe() {
		return heredaDe;
	}

	public void setHeredaDe(String c){
		heredaDe = c;
	}

	public Metodo getMetodo(Metodo m){
		return Tmetodos.get(m);
	}
	
	public Metodo getMetodo(String m){
		return Tmetodos.get(m);
	}
	
	public Atributo getAtributo(String m){
		for(Atributo a: Tatributos.values()){
			if(a.getNombre().equals(m))
				return a;
		}
			return null;
	}
		

	public Unidad getUnidadActual() {
		return unidadActual;
	}
	
	public void setUnidadActual(Unidad u){
		unidadActual=u;
	}

	public Constructor getConstructor() {
		return constructor;
	}
	
	public Map<String, Atributo> getAtributos(){
		return Tatributos;
	}
	
	public Map<String, Metodo> getMetodos(){
		return Tmetodos;
	}
	
	public TablaDeSimbolos getTablaSimb() {
		return tablaSimb;
	}
	
	public int getNroLinea(){
		return nroLinea;
	}
	
	public void tieneMain(){
		tieneMain=true;
	}
	
	public boolean estaConsolidada(){
		return consolidada;
	}

	public void setConsolidada(){
		consolidada=true;
	}
	
	public void chequearMetodos() throws Exception{
		
		if (!nombre.equals("System") && !nombre.equals("Object")) {
			if (constructor != null)
					constructor.chequearBloque();
					for (Metodo e : Tmetodos.values()) {	
						System.out.println("VOY A TRABAJAR CON EL METODO " + e.getNombre());
						Metodo metodoEnPadre= tablaSimb.getClase(heredaDe).getMetodo(e.getNombre());
						if(metodoEnPadre==null) //no checkeo al metodo del padre q ya se controlo antes -> mi compi no permite la redefinicion de metodos
							e.chequearBloque();
					}
		}
	}
	
	public void insertarConstructor(Constructor c) throws Exception{ //hace referencia para cuando se agrega un constructor x defecto 
		if(constructor==null){
			if(c.getNombre().equals(nombre))
				constructor= c;
			else
				throw new Exception("El constructor de la clase "+ nombre + " debe tener el mismo nombre que la clase ");
		}
		else
			throw new Exception("La clase "+this.getNombre()+" ya cuenta con un constructor");
	}
	
	public void insertarMetodo(Metodo m) throws Exception{
		
		if(!Tmetodos.containsKey(m.getNombre())){
			Tmetodos.put(m.getNombre(), m);
		}
		else
			throw new Exception("El metodo "+m.getNombre()+" en la linea "+ m.getNroLinea() +" de la clase "+ nombre +" ya existe. No puede haber 2 metodos con el mismo nombre.");
	}
	
	public void insertarAtributo(Atributo a) throws Exception{	
	
		if(!Tatributos.containsKey(a.getNombre())){
			Tatributos.put(a.getNombre(), a);
		}
		else
			throw new Exception("El atributo "+a.getNombre()+" en la linea "+ a.getNroLinea() +" ya existe. No puede haber 2 atributos con el mismo nombre.");
	}
	
	public void herenciaCircular() throws Exception{
	String nombrePadre= heredaDe;
	
	ArrayList<String> visitados= new ArrayList<String>();
	
	boolean circular=false;
	boolean termine=false;
	
		while(!circular && !termine){
			if(nombre.equals(nombrePadre) || visitados.contains(nombrePadre))
				circular=true;
			else{
				if(nombrePadre.equals("Object") || tablaSimb.getClase(nombrePadre)==null)
					termine=true;
				else{
					visitados.add(nombrePadre);
					nombrePadre = tablaSimb.getClase(nombrePadre).getHeredaDe();
					
				}
			}
		}		
		
		if(circular)
			throw new Exception("Se ha detectado herencia circular con la clase "+ nombrePadre + " en la linea " + tablaSimb.getClase(nombrePadre).getNroLinea());

	}
	

	public void actualizarAtributos() throws Exception{ //checkea que la clase no tenga atributos nombrados igual q su ancestro y los pone dentro de sus atributos
		
		Map<String, Atributo> atributosPadre= tablaSimb.getClase(heredaDe).getAtributos();
		Map<String, Atributo> agregar= new HashMap<String,Atributo>();
		
		for(Atributo a: atributosPadre.values()){
			if(Tatributos.containsKey(a.getNombre()))
					throw new Exception("La clase "+ nombre +" en la linea "+ Tatributos.get(a.getNombre()).getNroLinea()+" posee el atributo "+ a.getNombre()+ " definido en su ancestro");
			else
				if(!a.getVisibilidad().equals("private") && a.getVisibilidad()!=null) //si el atributo padre no es privado entonces ahora tambien forma parte de su hijo
					agregar.put(a.getNombre(),a);		
		}		
		
		Tatributos.putAll(agregar);
	}

	
	public void actualizarMetodos() throws Exception{
		Map<String, Metodo> metodosPadre= tablaSimb.getClase(heredaDe).getMetodos();
		Map<String, Metodo> agregar= new HashMap<String,Metodo>();
		
		
		for(Metodo a: metodosPadre.values()){	
			
			if(!Tmetodos.containsKey(a.getNombre()) && !a.getNombre().equals("main")){
				agregar.put(a.getNombre(),a); 		//todos los metodos se ponen en el hijo pq son publicos
			}
			else
				if(a.getNombre().equals("main"))
					throw new Exception("Solo puede haber un metodo main, no puede heredarse");
				else{

					Metodo metodo = this.Tmetodos.get(a.getNombre()); //agarro al metodo con ese nombre de ESTA clase 
					
					if (!metodo.equalsM(a))
						throw new Exception("El metodo "+metodo.getNombre()+" de la linea "+ metodo.getNroLinea() +" esta mal redefinido ");
				}
		}
	
		
		Tmetodos.putAll(agregar);

	}
	
	public void consolidar() throws Exception{
		
		if(!heredaDe.equals("Object") && !heredaDe.equals(null)){
			if(tablaSimb.getClase(heredaDe).estaConsolidada()==false){
				tablaSimb.getClase(heredaDe).consolidar();
			}
			

			actualizarAtributos();
			actualizarMetodos();
		}

		consolidada=true;
	}
		
}
