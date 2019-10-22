
public abstract class NodoExpresion {

	protected Token token;
	
	
	public NodoExpresion(Token t){
		token=t;
	}
	
	public Token getToken(){
		return token;
	}
	
	public abstract TipoMetodo chequear() throws Exception;

	public abstract void generar(TablaDeSimbolos t);
}
