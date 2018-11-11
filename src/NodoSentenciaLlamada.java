
public class NodoSentenciaLlamada extends NodoSentencia{
//nodo primario
	
	private NodoExpresion sentencia;
	
	public NodoSentenciaLlamada(Token t, NodoExpresion np){
		super(t);
		sentencia=np;
	}
	
	public void chequear() throws Exception{
		sentencia.chequear();
	}
	
	public void setSentencia(NodoExpresion np){
		sentencia= np;
	}
	
	public void setToken(Token t){
		token=t;
	}
}
