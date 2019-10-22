
public class TChar extends Tipo{

	public TChar(String nombre) {
		super(nombre);
	}
	
	public String getTipo(){
		return super.getTipo();
	}
	
	public boolean esSubTipo(TipoMetodo t){
		return t.getTipo().equals("char");
	}

}
