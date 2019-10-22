import java.util.ArrayList;
import java.util.LinkedList;

public class NodoLlamadaDirecta extends NodoOperandoPrimario {
	
	private LinkedList<NodoExpresion> argumentos;
	private Unidad met;
	private Clase clase;
	
	public NodoLlamadaDirecta(Token t, NodoEncadenado ne, Clase c, Unidad ua){
		super(t,ne);
		setClase(c);
		met= ua;
		setArgumentos(new LinkedList<NodoExpresion>());
	}

	public LinkedList<NodoExpresion> getArgumentos() {
		return argumentos;
	}

	public void setEncadenado(NodoEncadenado e){
		encadenado=e;
	}
	
	public void setArgumentos(LinkedList<NodoExpresion> argumentos) {
		this.argumentos = argumentos;
	}

	public Clase getClase() {
		return clase;
	}

	public void setClase(Clase clase) {
		this.clase = clase;
	}
	
	public TipoMetodo chequear() throws Exception{

		
		Metodo e = clase.getMetodo(token.getLexema());
		
		if (e != null) {
			if (met.getFormaMetodo() != null && met.getFormaMetodo().equals("static"))
				if (e.getFormaMetodo().equals("dynamic"))
					throw new Exception("Error en llamada a metodo '"+token.getLexema()+"'. No se puede llamar a un metodo dinamico en un metodo estatico. Linea "+token.getNroLinea());

			chequearArgumentos(e.getParametros());
			met=e;
			if (this.getEncadenado() == null){
				return e.getTipo();
			}
			else
				return this.getEncadenado().chequear(e.getTipo());
		}
		else {
			throw new Exception("Error en llamada a metodo '"+token.getLexema()+"', en la clase o no existe. Linea "+token.getNroLinea());
		}
	}
	
	private boolean chequearArgumentos(ArrayList<Parametro> lista) throws Exception{
		
		//Chequea que los tipos de los parametros sean compatibles con los tipos de los parametros formales del metodo
		
		if(lista.size() == argumentos.size()){
			int i = 0;
			while(i < lista.size()){
				if(!argumentos.get(i).chequear().conformidad(lista.get(i).getTipo())){
					throw new Exception("Error en llamada a metodo '"+token.getLexema()+"', se espera parametro tipo "+lista.get(i).getTipo().getTipo()+" o subtipo. Linea "+token.getNroLinea());
				}
				i++;
			}
			return true;
		}
		else
			throw new Exception("Error en llamada a metodo "+token.getLexema()+", la cantidad de parámetros es incorrecta. Linea "+token.getNroLinea());
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generar(TablaDeSimbolos t) {
		if(met.getFormaMetodo() != null && met.getFormaMetodo().equals("static")){	
			
			if(met.getTipo().getTipo() != null && !met.getTipo().getTipo().equals("void")){
				t.generarInstruccion("RMEM 1");
			}	
			int i = argumentos.size()-1;			
			while(i >= 0){
				argumentos.get(i).generar(t);
				i--;
			}
			t.generarInstruccion("PUSH "+ met.getLabel());				
		}
		
		else{
			t.generarInstruccion("LOAD 3");	
			if(met.getTipo() != null && !met.getTipo().getTipo().equals("void")){
				t.generarInstruccion("RMEM 1");
				t.generarInstruccion("SWAP");
			}			
			int i = argumentos.size()-1;			
				while(i >= 0){
					argumentos.get(i).generar(t);
					t.generarInstruccion("SWAP");
					i--;
				}	
			t.generarInstruccion("DUP");
			t.generarInstruccion("LOADREF 0");	
			t.generarInstruccion("LOADREF "+ met.getOffsetVT());					
		}		
		t.generarInstruccion("CALL");			
		if(this.encadenado != null)
			this.encadenado.generar(t);
		
	}
	
}
