
public abstract class TipoMetodo {

	protected String nombre;
	protected int nroLinea;
	
	public TipoMetodo(String nomb){
		nombre=nomb;
	}
	
	public boolean conformidad(TipoMetodo t) throws Exception{
		return esSubTipo(t);
	}
	
	public abstract boolean esSubTipo(TipoMetodo t) throws Exception;
	
	public String getTipo(){ //pues el tipo es el nombre del tipo
		return nombre;
	}
	
	public int getNroLinea(){
		return nroLinea;
	}
	
}
