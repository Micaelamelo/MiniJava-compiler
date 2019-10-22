import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TablaDeSimbolos {

	private ArrayList<Clase> clases;
	private Clase claseActual;
	private Clase claseConMain;
	private NodoBloque bloqueActual;
	private int i=1;
	private File out;
	private FileWriter archivoEscritura;
	
	public TablaDeSimbolos(String salida){
		claseActual = null;
		bloqueActual=null;
		clases = new ArrayList<Clase>();
		try {
			cargarPredefinidos();
		} catch (Exception e) {
			e.getMessage();
		}
		
		try {
			out = new File(salida);

			archivoEscritura = new FileWriter(out);
			
		//	System.setOut(new PrintStream(salida));
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
		//c.setLabel("ctr_object");
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
		e1.setOffsetRA(0);
		e1.setLabel("system_read");
		
		Metodo e2 = new Metodo("printB", this, Tvoid, "static",0);
		Parametro p = new Parametro("b", booleano, 0);
		e2.insertarParametro(p);
		System.insertarMetodo(e2);
		e2.setOffsetRA(1);
		e2.setLabel("system_printB");
		
		Metodo e3 = new Metodo("printC",this, Tvoid, "static",0);
		p = new Parametro("c", Tchar,0);
		e3.insertarParametro(p);
		System.insertarMetodo(e3);
		e3.setOffsetRA(2);
		e3.setLabel("system_printC");
		
		Metodo e4 = new Metodo("printI", this, Tvoid, "static",0);
		p = new Parametro("i", entero,0);
		e4.insertarParametro(p);
		System.insertarMetodo(e4);
		e4.setOffsetRA(3);
		e4.setLabel("system_printI");

		Metodo e5 = new Metodo("printS", this, Tvoid, "static",0);
		p = new Parametro("s", cadena,0);
		e5.insertarParametro(p);
		System.insertarMetodo(e5);
		e5.setOffsetRA(4);
		e5.setLabel("system_printS");
		
		Metodo e6 = new Metodo("println", this, Tvoid, "static",0);
		System.insertarMetodo(e6);
		e6.setOffsetRA(5);
		e6.setLabel("system_println");
		
		Metodo e7 = new Metodo("printBln",this, Tvoid, "static",0);
		p = new Parametro("b", booleano,0);
		e7.insertarParametro(p);
		System.insertarMetodo(e7);
		e7.setOffsetRA(6);
		e7.setLabel("system_printBln");
		
		Metodo e8 = new Metodo("printCln",this, Tvoid, "static",0);
		p = new Parametro("c", Tchar,0);
		e8.insertarParametro(p);
		System.insertarMetodo(e8);
		e8.setOffsetRA(7);
		e8.setLabel("system_printCln");
		
		Metodo e9 = new Metodo("printIln", this, Tvoid, "static",0);
		p = new Parametro("i", entero,0);
		e9.insertarParametro(p);
		System.insertarMetodo(e9);		
		e9.setOffsetRA(8);
		e9.setLabel("system_printIln");

		Metodo e10 = new Metodo("printSln", this, Tvoid, "static",0);
		e10.setOffsetRA(9);
		e10.setLabel("system_printSln");
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
				
				for(Metodo m: c.getMetodos()){
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
				if(c.estaConsolidada()==false)
					c.consolidar();
				} 
		}
	}
	
	public void chequearBloques() throws Exception{
		for(Clase e : clases) {
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
		for(Clase c: clases){
			if(!c.getNombre().equals("System"))
				c.generar();
		}
	}
	
	
	
	public void generarInstruccion(String codigo){
		try {
			archivoEscritura.write(codigo+'\n');
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getEtiqueta(){
		String etiqueta = "etiqueta";		
		etiqueta =etiqueta +i;
		i++;		
		return etiqueta;
		
	}
	
	public void cerrarArchivo(){
		try {
			archivoEscritura.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void inicializar(){
		try{
				
			generarInstruccion(".CODE");

			generarInstruccion("PUSH simple_heap_init");
			generarInstruccion("CALL");
			generarInstruccion("PUSH EtiquetaMain" );
			generarInstruccion("CALL");
			generarInstruccion("HALT" );

			//CODIGO SIMPLE HEAP INIT DE LA CATEDRA
			generarInstruccion("simple_heap_init: ");
			generarInstruccion("RET 0	; Retorna inmediatamente");
			
			//CODIGO SIMPLE MALLOC DE LA CATEDRA
			generarInstruccion("simple_malloc: ");
			generarInstruccion("LOADFP ; inicializacion unidad" );
			generarInstruccion("LOADSP ");
			generarInstruccion("STOREFP ; Finaliza inicialización del RA");
			generarInstruccion("LOADHL ; hl");
			generarInstruccion("DUP ; hl");
			generarInstruccion("PUSH 1; 1");
			generarInstruccion("ADD ; hl+1" );
			generarInstruccion("STORE 4; Guarda el resultado (un puntero a la primer celda de la región de memoria)");
			generarInstruccion("LOAD 3; Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)" );
			generarInstruccion("ADD" );
			generarInstruccion("STOREHL ; Mueve el heap limit (hl). Expande el heap");
			generarInstruccion("STOREFP ");
			generarInstruccion("RET 1 ; Retorna eliminando el parámetro");
			generarInstruccion(".CODE" );		
			
			generarInstruccion("system_read: ");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("READ");
			generarInstruccion("PUSH 48");
			generarInstruccion("SUB");
			generarInstruccion("STORE 3");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 0");

			generarInstruccion("system_printi: ");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("IPRINT");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");	
			
			generarInstruccion("system_printiln: ");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("IPRINT");
			generarInstruccion("PRNLN");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");
			
			generarInstruccion("system_printb: ");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("BPRINT");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");		
			
			generarInstruccion("system_printbln:");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("BPRINT");
			generarInstruccion("PRNLN");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");
			
			generarInstruccion("system_printc: ");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("CPRINT");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");
			
			generarInstruccion("system_printcln:");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("CPRINT");
			generarInstruccion("PRNLN");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");
			
			generarInstruccion("system_prints: ");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("SPRINT");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");
			
			generarInstruccion("system_printsln:");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("LOAD 3");
			generarInstruccion("SPRINT");
			generarInstruccion("PRNLN");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 1");
			
			generarInstruccion("system_println:");
			generarInstruccion("LOADFP");
			generarInstruccion("LOADSP");
			generarInstruccion("STOREFP");
			generarInstruccion("PRNLN");
			generarInstruccion("STOREFP");
			generarInstruccion("RET 0");
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
}
