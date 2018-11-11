
public class TABoolean extends Array {

	public TABoolean(String tipoArray) {
		super(tipoArray);
	}
	
	public boolean esSubTipo(TipoMetodo t){
		return t.getTipo().equals("TAboolean");
	}


}
