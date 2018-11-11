
public abstract class NodoConstructor extends NodoOperandoPrimario{

	private String nombre;
	
	public NodoConstructor(Token t, NodoEncadenado e, String n) {
		super(t, e);
		setNombre(n);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	

}
