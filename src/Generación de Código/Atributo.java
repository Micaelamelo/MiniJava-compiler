
public class Atributo {

	private String nombre;
	private Tipo tipo;
	private String visibilidad;
	private int offset;
	private int nroLinea;

	public Atributo(String nom, Tipo t, String vis, int n){
		nombre=nom;
		tipo=t;	
		visibilidad=vis;
		nroLinea=n;
		offset=0;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int off) {
		offset = off;
	}

	public String getNombre(){
		return nombre;
	}
	
	public int getNroLinea(){
		return nroLinea;
	}
	
	public Tipo getTipo(){
		return tipo;
	}
	
	public String getVisibilidad(){
		return visibilidad;
	}	
	
}
