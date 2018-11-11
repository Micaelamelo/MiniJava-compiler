
public class TInt extends Tipo {

	public TInt(String nombre) {
		super(nombre);
	}
	
	public String getTipo(){
		return super.getTipo();
	}

	public boolean esSubTipo(TipoMetodo t){
		return t.getTipo().equals("int");
	}
}

