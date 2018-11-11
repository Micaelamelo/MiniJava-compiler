import java.util.ArrayList;
import java.util.LinkedList;

public class NodoLlamadaEstatica extends NodoOperandoPrimario{

	private Token tokenLlamada;
	private TablaDeSimbolos tabla;
	private LinkedList<NodoExpresion> argumentos;
	private String clase;
	
	public NodoLlamadaEstatica(Token t, Token tl, TablaDeSimbolos ts, String c, NodoEncadenado e){
		super(t,e);
		setTokenLlamada(tl);
		setTabla(ts);
		argumentos=new LinkedList<NodoExpresion>();
		setClase(c);
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public LinkedList<NodoExpresion> getArgumentos() {
		return argumentos;
	}

	public void setArgumentos(LinkedList<NodoExpresion> argumentos) {
		this.argumentos = argumentos;
	}

	public TablaDeSimbolos getTabla() {
		return tabla;
	}

	public void setTabla(TablaDeSimbolos tabla) {
		this.tabla = tabla;
	}

	public Token getTokenLlamada() {
		return tokenLlamada;
	}

	public void setTokenLlamada(Token tokenLlamada) {
		this.tokenLlamada = tokenLlamada;
	}
	
	public TipoMetodo chequear() throws Exception{
		Clase a = tabla.getClase(token.getLexema());
		
		if (a != null) {
			Metodo m = a.getMetodo(tokenLlamada.getLexema()); 
			if (m != null){
				if (m.getFormaMetodo().equals("static")) {
					chequearArgumentos(m.getParametros());
					if (encadenado != null)
						return encadenado.chequear(m.getTipo());
					else
						return m.getTipo();
				}
				else
					throw new Exception("Error semantico en llamada a '"+tokenLlamada.getLexema()+"', el metodo debe ser estatico. Linea "+token.getNroLinea());
			}
			else 
				throw new Exception("Error semantico en llamada a '"+tokenLlamada.getLexema()+"', el metodo no existe. Linea "+token.getNroLinea());
		}		
		else {
				throw new Exception("Error semantico, la clase '"+token.getLexema()+"' no existe. Linea "+token.getNroLinea());
			}
		
	}
	
	private boolean chequearArgumentos(ArrayList<Parametro> lista) throws Exception{
		
		//Chequea que los tipos de los parametros sean compatibles con los tipos de los parametros formales del metodo
		
		if(lista.size() == argumentos.size()){
			int i = 0;
			while(i < lista.size()){
				if(!argumentos.get(i).chequear().conformidad(lista.get(i).getTipo())){
					throw new Exception("Error semantico en llamada a metodo "+tokenLlamada.getLexema()+", se espera parametro tipo "+lista.get(i).getTipo().getTipo()+" o subtipo. Linea "+tokenLlamada.getNroLinea());
				}
				i++;
			}
			return true;
		}
		else
			throw new Exception("Error semántico en llamada a metodo "+tokenLlamada.getLexema()+", la cantidad de parámetros es incorrecta. Linea "+tokenLlamada.getNroLinea());
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
