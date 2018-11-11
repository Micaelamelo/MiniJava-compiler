import java.util.ArrayList;
import java.util.LinkedList;

public class NodoEncadenadoLlamada extends NodoEncadenado{

	private LinkedList<NodoExpresion> argumentos;

	public NodoEncadenadoLlamada(Token t, TablaDeSimbolos tt, NodoEncadenado en){
		super(t,tt, en);
		argumentos=new LinkedList<NodoExpresion>();
	}
	
	
	public TipoMetodo chequear(TipoMetodo t) throws Exception{

		Clase c = ts.getClase(t.getTipo());
		System.out.println("encontro clase ");
		
		if (c != null) {
			Metodo m = c.getMetodo(token.getLexema());
			if (m != null) {
				chequearArgumentos(m.getParametros());
				TipoMetodo tipoM = m.getTipo();
				if (encadenado != null) {
					return encadenado.chequear(tipoM);
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
				if(!argumentos.get(i).chequear().equals(lista.get(i).getTipo())){
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

	@Override
	public Tipo chequear() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
