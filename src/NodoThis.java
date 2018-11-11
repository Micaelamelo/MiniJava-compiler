
public class NodoThis extends NodoAcceso{

	private Clase clase;
	private Unidad metodo;
	private TablaDeSimbolos ta;
	private boolean esIzq;
	
	public NodoThis(Token t, NodoEncadenado e, Clase c, Unidad m, TablaDeSimbolos te){
		super(t,e);
		metodo=m;
		clase=c;
		esIzq=false;
		ta=te;
	}
	
	public void setIzq(){
		esIzq=true;
	}
	
	public TipoMetodo chequear() throws Exception{
		
		if(metodo.getFormaMetodo().equals("static"))
			throw new Exception("No puede asignarse un this en static");
				
		TIdClase t= new TIdClase(ta, clase.getNombre());
		
		if(esIzq==true && encadenado==null){
			throw new Exception("No es posible asignarle valores a This");
		}
		else{
			if(encadenado==null){
					return t;
			}
			else{
				//TipoMetodo t;
				//System.out.println("existe encadenado? "+encadenado.chequear());
				return encadenado.chequear(t);
				}
		}
	}

	@Override
	public TipoMetodo chequear(TipoMetodo t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
