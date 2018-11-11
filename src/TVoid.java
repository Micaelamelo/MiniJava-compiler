
public class TVoid extends TipoMetodo{
	
	public TVoid(String nombre) {
		super(nombre);
	}
	
	public boolean esSubTipo(TipoMetodo t){
		
		return t.getTipo().equals("void");
	}

}
