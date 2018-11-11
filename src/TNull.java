
public class TNull extends Tipo{

	public TNull(String nombre) {
		super(nombre);
	}
	

	public boolean esSubTipo(TipoMetodo t){

		if (t instanceof TInt || t instanceof TChar || t instanceof TBoolean)
			return false;
		else
			return true;
	
	}
}
