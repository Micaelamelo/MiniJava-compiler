import java.util.ArrayList;
import java.util.LinkedList;

public class NodoEncadenadoLlamada extends NodoEncadenado{

	private LinkedList<NodoExpresion> argumentos;
	private Metodo met;
	
	public NodoEncadenadoLlamada(Token t, TablaDeSimbolos tt, NodoEncadenado en){
		super(t,tt, en);
		met=null;
		argumentos=new LinkedList<NodoExpresion>();
	}
	
	
	public TipoMetodo chequear(TipoMetodo t) throws Exception{

		Clase c = ts.getClase(t.getTipo());
		
		if (c != null) {
			Metodo m = c.getMetodo(token.getLexema());
			if (m != null) {
				chequearArgumentos(m.getParametros());
				TipoMetodo tipoM = m.getTipo();
				met=m;
				if (this.getEncadenado() != null) {
					return this.getEncadenado().chequear(tipoM);
				}
				else
					return tipoM;	
			}
			else {
				throw new Exception("Error en llamada encadenada. El metodo '"+token.getLexema()+"' no existe en la clase '"+t.getTipo()+" Linea "+token.getNroLinea());
			}		
		}
		else {
			throw new Exception("Error en llamada encadenada. Linea "+token.getNroLinea());
		}
	}
	
	private boolean chequearArgumentos(ArrayList<Parametro> lista) throws Exception{
		
		//Chequea que los tipos de los parametros sean compatibles con los tipos de los parametros formales del metodo
		
		if(lista.size() == argumentos.size()){
			int i = 0;
			while(i < lista.size()){
				if(!argumentos.get(i).chequear().conformidad(lista.get(i).getTipo())){
					throw new Exception("Error en llamada a unidad '"+token.getLexema()+"', se espera parametro tipo "+lista.get(i).getTipo().getTipo()+" o subtipo. Linea "+token.getNroLinea());
				}
				i++;
			}
			return true;
		}
		else
			throw new Exception("Error en llamada a metodo "+token.getLexema()+", la cantidad de parámetros no es igual. Linea "+token.getNroLinea());
	}

	public LinkedList<NodoExpresion> getArgumentos() {
		return argumentos;
	}

	public void setArgumentos(LinkedList<NodoExpresion> argumentos) {
		this.argumentos = argumentos;
	}

	public void setIzq() {
		if (this.getEncadenado() != null) {
			this.getEncadenado().setIzq();
		}
	}
	
	@Override
	public Tipo chequear() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void generar(TablaDeSimbolos t) {
	
		
		
		if(met.getFormaMetodo().equals("static")){ //si el metodo a llamar es estatico, es IGUAL a nodo llamada estatica

			t.generarInstruccion("POP");
			
			if(!met.getTipo().getTipo().equals("void"))
				t.generarInstruccion("RMEM 1");
			
			for(int i= argumentos.size() -1; i >= 0; i --)
				argumentos.get(i).generar(t);
			
			t.generarInstruccion("PUSH "+ met.getLabel());	
			t.generarInstruccion("CALL");		
		}
		
		else{
		
			if (!met.getTipo().getTipo().equals("void")) {
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
//		System.out.println("el metodo "+met.getNombre()+" tiene oset "+met.getOffsetVT());
		t.generarInstruccion("LOADREF "+met.getOffsetVT());
		t.generarInstruccion("CALL");	
		
		}
		
		if (this.getEncadenado() != null) {
			this.getEncadenado().generar(t);
		}
		
	}
	
}
