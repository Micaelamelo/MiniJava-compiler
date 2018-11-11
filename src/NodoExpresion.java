
public abstract class NodoExpresion {
//chequear devuelve un tipo
	protected Token token;
	
	
	public NodoExpresion(Token t){
		token=t;
	}
	
	public Token getToken(){
		return token;
	}
	
	public abstract TipoMetodo chequear() throws Exception;

}
