
public class Parametro {
	
	private Tipo tipo;
	private String nombre;
	private int offset;
	private int nroLinea;
	
	public Parametro(String nom, Tipo t, int n){
		nombre=nom;
		tipo=t;
		offset=0;
		nroLinea=n;
	}
	
	public Tipo getTipo(){
		return tipo;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int off) {
		offset = off;
	}
	
	public int getNroLinea(){
		return nroLinea;
	}


}
