
public abstract class NodoSentencia {

	protected Token token;
	
	public NodoSentencia(Token ti){
		token=ti;
	}
	
	public abstract void chequear() throws Exception;
	
	public abstract void generar(TablaDeSimbolos t);
}
