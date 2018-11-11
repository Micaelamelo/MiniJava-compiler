
public class TAChar extends Array {

	public TAChar(String tipoArray) {
		super(tipoArray);
	}
	

	public boolean esSubTipo(TipoMetodo t){
		return t.getTipo().equals("TAchar");
	}
}
