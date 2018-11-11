
public class TAInt extends Array {

	public TAInt(String tipoArray) {
		super(tipoArray);
	}

	public boolean esSubTipo(TipoMetodo t){
		return t.getTipo().equals("TAint");
	}

}
