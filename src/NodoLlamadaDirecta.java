import java.util.ArrayList;
import java.util.LinkedList;

public class NodoLlamadaDirecta extends NodoOperandoPrimario {
	
	private LinkedList<NodoExpresion> argumentos;
	private Clase clase;
	
	public NodoLlamadaDirecta(Token t, NodoEncadenado ne, Clase c){
		super(t,ne);
		setClase(c);
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
			chequearArgumentos(e.getParametros());
			if (encadenado == null){
				return e.getTipo();
			}
			else
				return encadenado.chequear(e.getTipo());
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
	
}
