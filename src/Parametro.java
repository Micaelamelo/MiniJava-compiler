
public class Parametro {
	
	private Tipo tipo;
	private String nombre;
	
	private int nroLinea;
	
	public Parametro(String nom, Tipo t, int n){
		nombre=nom;
		tipo=t;
		nroLinea=n;
	}
	
	public Tipo getTipo(){
		return tipo;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public int getNroLinea(){
		return nroLinea;
	}


}
