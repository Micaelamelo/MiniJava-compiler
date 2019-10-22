
public class TBoolean extends Tipo {

	public TBoolean(String nombre) {
		super(nombre);
	}
	

	public boolean esSubTipo(TipoMetodo t){
		return t.getTipo().equals("boolean");
	}
}
