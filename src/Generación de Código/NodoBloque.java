import java.util.LinkedList;

public class NodoBloque extends NodoSentencia{
//lista de sentencias
//ref a bloque padre q lo contiene y lista de las var locales q se declaran en el bloque
	
	private LinkedList<NodoSentencia> sentencias;
	private LinkedList<Atributo> variablesLocales;
	private Unidad metodoBloque;
	private NodoBloque bloquePadre;
	private Clase clase;
	private int offsetVarLocal; //offset de las variables locales del bloque
	
	public NodoBloque(Token t, NodoBloque padre, Unidad mb, Clase c){
		super(t);
		bloquePadre = padre;
		metodoBloque=mb;
		clase=c;
		offsetVarLocal=0;
		sentencias = new LinkedList<NodoSentencia>();
		variablesLocales = new LinkedList<Atributo>();
	}
	
	public LinkedList<NodoSentencia> getSentencias() {
		return sentencias;
	}
	public void setSentencias(LinkedList<NodoSentencia> sentencias) {
		this.sentencias = sentencias;
	}
	public LinkedList<Atributo> getVariablesLocales() {
		return variablesLocales;
	}
	public void setVariablesLocales(LinkedList<Atributo> variablesLocales) {
		this.variablesLocales = variablesLocales;
	}
	public NodoBloque getBloquePadre() {
		return bloquePadre;
	}
	public void setBloquePadre(NodoBloque bloquePadre) {
		this.bloquePadre = bloquePadre;
	}
	
	public void chequear() throws Exception{ 
		pasarVariablesPadre();
		for(NodoSentencia s: sentencias){
		//	System.out.println("voy a chequear sentencia: "+ s.toString().toString().toString());
			s.chequear();
		}
	}
	
	private void pasarVariablesPadre() {
		if (bloquePadre != null) {
			for(Atributo e : bloquePadre.getVariablesLocales())
				variablesLocales.addLast(e);
		}
	}
	
	public void insertarVariableLocal(Atributo e) throws Exception {
		if(existeVariable(e.getNombre()) == null){
			if (existeParametro(e.getNombre()) == null) {
				variablesLocales.add(e);
				if (bloquePadre != null) {
					e.setOffset(bloquePadre.getoffsetVarLocal()+offsetVarLocal);
				}
				else{
					e.setOffset(offsetVarLocal);					
				}
				offsetVarLocal--; //las variables locales van a ser negativas
			}
			else {
				throw new Exception("Error, no se puede declarar la variable '"+e.getNombre()+"'. Ya existe un parametro con ese nombre. Linea "+e.getNroLinea());
			}
		}
		else
			throw new Exception("Error, ya existe una variable local con nombre '"+e.getNombre()+"'. Linea "+e.getNroLinea());
	}
	
	private int getoffsetVarLocal() {
		return offsetVarLocal;
	}

	public Parametro existeParametro(String nombre) {
		for (Parametro e : metodoBloque.getParametros()) {
			if (e.getNombre().equals(nombre))
				return e;
		}
		return null;
	}

	
	public Atributo existeVariable(String nombre)  {	
		for(Atributo e2 : variablesLocales) {
			
			if (e2.getNombre().equals(nombre)){
				return e2;
			}
		}
		return null;
	}
	
	public Atributo existeAtributoInstancia(Token var) throws Exception{

		for (Atributo e : clase.getAtributos().values()) {
		
			//if(e.getVisibilidad().equals("public")){
				if (e.getNombre().equals(var.getLexema())) {
					
					String forma = metodoBloque.getFormaMetodo();
					if (forma != null){
						if (forma.equals("static")) {
							throw new Exception("Error en llamada a '"+e.getNombre()+"', no se puede utilizar una variable de instancia en un metodo estatico. Linea "+var.getNroLinea());
						}
						else{
							
							return e;
						}
					}
					else
						return e;
				}
		//	}
		}
	
		return null;
		
	}
	
	public void insertarSentencia(NodoSentencia nS) {
		sentencias.addLast(nS);		
	}
	
	public LinkedList<Atributo> getAtributosLocales(){
		return variablesLocales;
	}
	

	@Override
	public void generar(TablaDeSimbolos t) {
		for (NodoSentencia s: sentencias){
	//		System.out.println("El nodo sentencia es: "+s);
			s.generar(t);
		}

		if (bloquePadre != null)
			t.generarInstruccion("FMEM "+(variablesLocales.size()-bloquePadre.getVariablesLocales().size()));
		else
			t.generarInstruccion("FMEM "+variablesLocales.size());
		
	}
	
	
	

}
