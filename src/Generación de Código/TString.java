
public class TString extends Tipo{

	public TString(String nombre) {
		super(nombre);
	}
	
	public String getTipo(){
		return super.getTipo();
	}
	
	public boolean esSubTipo(TipoMetodo t){
		if(t.getTipo().equals("null")) return true;
		return t.getTipo().equals("String");
	}
}
