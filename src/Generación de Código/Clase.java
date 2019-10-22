import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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
	private int offsetVT, CIR;
	
	protected Map<String, Metodo> Tmetodos;

	protected LinkedList<Metodo> metodos;
	
	protected Map<String, Atributo> Tatributos;
	
	private LinkedList<Metodo> metodosDeLaClase;
	private LinkedList<Atributo> atributosDeLaClase;

	
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
		metodosDeLaClase = new LinkedList<Metodo>();

		metodos= new LinkedList<Metodo>();
		atributosDeLaClase= new LinkedList<Atributo>();
		nroLinea=n;
		
		offsetVT = 0;
		CIR = 1;
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
		for(Metodo e : metodos) {
			if (e.getNombre().equals(nombre))
				return e;
		}		
		return null;	
	}
	
	public Metodo getMetodo(String m){
		for(Metodo e : metodos) {
			if (e.getNombre().equals(m))
				return e;
		}		
		return null;
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
	
	public LinkedList<Atributo> getAtributosL(){
		return atributosDeLaClase;
	}
	
	public LinkedList<Metodo> getMetodos(){
		return metodos;
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
				//		System.out.println("VOY A TRABAJAR CON EL METODO " + e.getNombre());
							e.chequearBloque();
					}
		}
	}
	
	public void insertarConstructor(Constructor c) throws Exception{ //hace referencia para cuando se agrega un constructor x defecto 
		if(constructor==null){
			if(c.getNombre().equals(nombre)){
				constructor= c;
				c.setLabel("CTR_"+nombre);
			}
			else
				throw new Exception("El constructor de la clase "+ nombre + " debe tener el mismo nombre que la clase ");
		}
		else
			throw new Exception("La clase "+this.getNombre()+" ya cuenta con un constructor");
	}
	
	public void insertarMetodo(Metodo m) throws Exception{
		String e;
		
		if(!Tmetodos.containsKey(m.getNombre())){			
			if (m.getFormaMetodo().equals("dynamic")) {
				m.setOffsetVT(offsetVT);
				offsetVT++;
			}
			
			metodos.add(m);
			
			Tmetodos.put(m.getNombre(), m);		
			metodosDeLaClase.add(m);
			
			if(m.getLabel() == null){
				if(!m.getNombre().equals("main"))
					e = m.getNombre()+"_"+nombre; //le pongo etiqueta con su nombre
				else
					e = "EtiquetaMain"; //si es main le pongo etiqueta: etiquetamain				
				m.setLabel(e);
			}
		}
		else
			throw new Exception("El metodo "+m.getNombre()+" en la linea "+ m.getNroLinea() +" de la clase "+ nombre +" ya existe. No puede haber 2 metodos con el mismo nombre.");
			
	}
	
	public void insertarAtributo(Atributo a) throws Exception{	
	
		if(!Tatributos.containsKey(a.getNombre())){
			Tatributos.put(a.getNombre(), a);
			atributosDeLaClase.addLast(a);
			a.setOffset(CIR);
			CIR++;
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
		
		LinkedList <Atributo> atributosPadre= tablaSimb.getClase(heredaDe).getAtributosL();
		Map<String, Atributo> agregar= new HashMap<String,Atributo>();
		Collections.reverse(atributosPadre);
		
		for(Atributo a: atributosPadre){
			if(Tatributos.containsKey(a.getNombre()))
					throw new Exception("La clase "+ nombre +" en la linea "+ Tatributos.get(a.getNombre()).getNroLinea()+" posee el atributo "+ a.getNombre()+ " definido en su ancestro");
			else
				if(!a.getVisibilidad().equals("private") && a.getVisibilidad()!=null){ //si el atributo padre no es privado entonces ahora tambien forma parte de su hijo
					agregar.put(a.getNombre(),a);		
					atributosDeLaClase.addFirst(a);
				}
		}		
		
		Tatributos.putAll(agregar);
/*
		int offPadre= tablaSimb.getClase(heredaDe).getAtributos().size();
		for(Atributo inst: atributosDeLaClase){
			if(!Tatributos.containsKey(inst.getNombre())){
				offPadre++;
				inst.setOffset(offPadre);
			}	
		}
		*/
		
		int i = 1;
		for (Atributo a : atributosDeLaClase) {
			a.setOffset(i);
			i++;
		}
		Collections.reverse(atributosPadre);
	}


	
	public void actualizarMetodos() throws Exception{
		LinkedList<Metodo> metodosPadre= tablaSimb.getClase(heredaDe).getMetodos();
		Collections.reverse(metodosPadre);
		
		
		for(Metodo a: metodosPadre){	
			
			if(!Tmetodos.containsKey(a.getNombre())  && !a.getNombre().equals("main")){
				metodos.addFirst(a); 		//todos los metodos se ponen en el hijo pq son publicos
		//		metodosDeLaClase.add(a);
			}
			else
				if(a.getNombre().equals("main"))
					throw new Exception("Solo puede haber un metodo main, no puede heredarse");
				else{

					Metodo metodo = this.getMetodo(a.getNombre()); //agarro al metodo con ese nombre de ESTA clase 
					
					if (!metodo.equalsM(a))
						throw new Exception("El metodo "+metodo.getNombre()+" de la linea "+ metodo.getNroLinea() +" esta mal redefinido ");
					else{
						metodos.remove(metodo);
					//	metodosDeLaClase.remove(metodo);
						metodos.addFirst(metodo);
					//	metodosDeLaClase.addFirst(metodo);
						metodo.setOffsetVT(a.getOffsetVT());
					}
				}
		}
		
		int i = 0;
		for (Metodo m : metodos) {
		
			if (m.getFormaMetodo().equals("dynamic")) {
				m.setOffsetVT(i);
				i++;
			}
		}
		
	
		Collections.reverse(metodosPadre);
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
	public int getOffsetVT() {
		return offsetVT;
	}
	
	public int cantMetodosDinamicos() {
		int cant = 0;
		for (Metodo e : metodos) {
			if (!e.getFormaMetodo().equals("static")) {
				cant++;
			}
		}
		return cant;
	}
	

	public void generar(){
	
		if (cantMetodosDinamicos() > 0) {
			tablaSimb.generarInstruccion(".DATA");
			tablaSimb.generarInstruccion("VT_"+nombre+ ": ");
			for(Metodo m : metodos){
					if(!m.getFormaMetodo().equals("static"))
						tablaSimb.generarInstruccion("DW "+m.getLabel());			
			}
		}
		
		if(constructor != null){
			tablaSimb.generarInstruccion(".CODE");
			tablaSimb.generarInstruccion(constructor.getLabel()+": ");
			tablaSimb.generarInstruccion("LOADFP");
			tablaSimb.generarInstruccion("LOADSP");
			tablaSimb.generarInstruccion("STOREFP");
			
			if(constructor.getBloque()!= null)
				constructor.getBloque().generar(tablaSimb);
				
			tablaSimb.generarInstruccion("LOAD 3");
			tablaSimb.generarInstruccion("STORE "+(3+constructor.getParametros().size() + 1));
			tablaSimb.generarInstruccion("STOREFP");
			tablaSimb.generarInstruccion("RET "+(constructor.getParametros().size() + 1));
		}
		
		for(Metodo m: metodosDeLaClase){
			m.generar(tablaSimb);
	}
	}
		
}
