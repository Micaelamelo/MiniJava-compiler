
public class Token {
	
	private String nombre;
	private String lexema;
	private int nroLinea;
	private int nroColumna;
	
	/**
	 * Constructor
	 * @param nom Nombre del token
	 * @param lex Lexema del token
	 * @param lin Nro de linea donde se encuentra el token
	 * @param col Nro de columna donde comienza el token
	 */
	public Token(String nom, String lex, int lin, int col) {
		nombre = nom;
		lexema = lex;
		nroLinea = lin;
		nroColumna=col;
	}
	
	/**
	 * Devuelve el nombre del token.
	 * @return nombre del token
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve lexema del token.
	 * @return lexema del token
	 */
	public String getLexema() {
		return lexema;
	}

	/**
	 * Devuelve el numero de linea donde se encuentra el token.
	 * @return numero de linea del token
	 */
	public int getNroLinea() {
		return nroLinea;
	}
	
	/**
	 * Devuelve el numero de columna donde comienza el token.
	 * @return numero de coulmna del inicio del token
	 */
	public int getNroColumna() {
		return nroColumna;
	}
}
