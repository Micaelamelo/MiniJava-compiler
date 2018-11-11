import java.util.ArrayList;

public class TablaDeSimbolos {

	private ArrayList<Clase> clases;
	private Clase claseActual;
	private Clase claseConMain;
	private NodoBloque bloqueActual;
	
	public TablaDeSimbolos(){
		claseActual = null;
		bloqueActual=null;
		clases = new ArrayList<Clase>();
		try {
			cargarPredefinidos();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public ArrayList<Clase> getClases() {
		return clases;
	}

	public Clase getClaseActual() {
		return claseActual;
	}
	
	public Clase getClaseConMain(){
		return claseConMain;
	}
	
	public void setClaseActual(Clase c){
		claseActual=c;
	}
	
	
	public Clase getClase(String c) throws Exception{
		for(Clase cs: clases){
			if(cs.getNombre().equals(c))
				return cs;
		}
		return null;
	}
	
	public void insertarClase(Clase c) throws Exception{ 	//no puede haber 2 clases con el mismo nombre ni pueden llamarse Object o System		
		if(!existeClase(c.getNombre())){
			if(!c.getNombre().equals("Object") && !c.getNombre().equals("System"))
				clases.add(c);
			else
				throw new Exception("Las clases no pueden llamarse System|Object");
		}
		else
			throw new Exception("La clase "+ c.getNombre() +" ya existe");
	}
	
	private void cargarPredefinidos() throws Exception{
		Clase Object = new Clase("Object", null, this, 0);
		clases.add(Object);
		Constructor c= new Constructor("Object", this, Object.getNroLinea());
		Object.insertarConstructor(c);
			
		Clase System = new Clase("System", "Object", this,0);
		clases.add(System);
		Constructor s= new Constructor("System", this, System.getNroLinea());
		System.insertarConstructor(s);

		Tipo entero=new TInt("int");
		Tipo booleano=new TBoolean("boolean");
		Tipo cadena= new TString("String");
		TipoMetodo Tvoid= new TVoid("void");
		Tipo Tchar= new TChar("char");
		 	
		
		Metodo e1 = new Metodo("read", this, entero, "static", 0);
		System.insertarMetodo(e1);
		
		Metodo e2 = new Metodo("printB", this, Tvoid, "static",0);
		Parametro p = new Parametro("b", booleano, 0);
		e2.insertarParametro(p);
		System.insertarMetodo(e2);
		
		Metodo e3 = new Metodo("printC",this, Tvoid, "static",0);
		p = new Parametro("c", Tchar,0);
		e3.insertarParametro(p);
		System.insertarMetodo(e3);
		
		Metodo e4 = new Metodo("printI", this, Tvoid, "static",0);
		p = new Parametro("i", entero,0);
		e4.insertarParametro(p);
		System.insertarMetodo(e4);

		Metodo e5 = new Metodo("printS", this, Tvoid, "static",0);
		p = new Parametro("s", cadena,0);
		e5.insertarParametro(p);
		System.insertarMetodo(e5);
		
		Metodo e6 = new Metodo("println", this, Tvoid, "static",0);
		System.insertarMetodo(e6);
		
		Metodo e7 = new Metodo("printBln",this, Tvoid, "static",0);
		p = new Parametro("b", booleano,0);
		e7.insertarParametro(p);
		System.insertarMetodo(e7);
		
		Metodo e8 = new Metodo("printCln",this, Tvoid, "static",0);
		p = new Parametro("c", Tchar,0);
		e8.insertarParametro(p);
		System.insertarMetodo(e8);
		
		Metodo e9 = new Metodo("printIln", this, Tvoid, "static",0);
		p = new Parametro("i", entero,0);
		e9.insertarParametro(p);
		System.insertarMetodo(e9);		

		Metodo e10 = new Metodo("printSln", this, Tvoid, "static",0);
		p = new Parametro("s", cadena,0);
		e10.insertarParametro(p);
		System.insertarMetodo(e10);
		
	}

	public boolean existeClase(String c){
		for(Clase cs: clases){
			if(cs.getNombre().equals(c))
				return true;
		}
		return false;
	}
	
	
	public void chequearTodo() throws Exception{
	boolean encontre=false;

		for(Clase c: clases){
			
			if(!c.getNombre().equals("Object") && !c.getNombre().equals("System")){
				
				if(c.getConstructor()==null)
					c.insertarConstructor(new Constructor(c.getNombre(),this,0));				
				
				for(Atributo a: c.getAtributos().values()){
					if(a.getTipo() instanceof TIdClase && !existeClase(a.getTipo().getTipo()))
						throw new Exception("No esta declarado el tipo del atributo "+a.getNombre()+ " en la linea "+a.getNroLinea() +" de la clase "+c.getNombre());
				}
				
				for(Metodo m: c.getMetodos().values()){
					if(m.getTipo() instanceof TIdClase && !existeClase(m.getTipo().getTipo()))
						throw new Exception("No esta declarado el tipo del metodo "+m.getNombre()+ " en la linea "+m.getNroLinea() +" de la clase "+c.getNombre());				
					else 
						if(m.getTipo() instanceof TVoid && m.getNombre().equals("main") && m.getParametros().size()==0){
							if(!encontre){
								encontre=true;
								c.tieneMain();
								if(claseConMain==null)
									claseConMain=c;
							}
							else
								throw new Exception("No se puede tener mas de un metodo Main");
						}
					
					for(Parametro p: m.getParametros())
						if(p.getTipo() instanceof TIdClase && !existeClase(p.getTipo().getTipo()))
							throw new Exception("No esta declarado el tipo del parametro "+p.getNombre()+ " del metodo "+ m.getNombre()+ " en la linea "+p.getNroLinea() +" de la clase "+c.getNombre());
				}
				
				c.herenciaCircular();	//hijo extiende a padre pero padre no extiende a hijo
				
			}
			
		}
			
		
		if(!encontre){
			throw new Exception("No se encontro metodo estatico main en alguna de las clases, "
				+ " por favor inserte una");
		}
	}

	public void consolidar2() throws Exception{
		
		
		for(Clase c: clases){
			if(!c.getNombre().equals("Object") && !c.getNombre().equals("System")){
				System.out.println("La clase "+c.getNombre()+" esta consolidada? "+c.estaConsolidada());
				if(c.estaConsolidada()==false)
					c.consolidar();
				} 
		}
	}
	
	public void chequearBloques() throws Exception{
		for(Clase e : clases) {
			System.out.println("ESTOY TRABAJANDO CON CLASE "+ e.getNombre());
			e.chequearMetodos();		
		}
	}

	public NodoBloque getBloqueActual() {
		return bloqueActual;
	}

	public void setBloqueActual(NodoBloque bloqueActual) {
		this.bloqueActual = bloqueActual;
	}
	
	public void generar(){
		
	}
	
	
}
