import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main {

	public static void main(String args[]) {

	//	long startTime=System.nanoTime();
		String archivo;
		
		if(args.length == 0)
			System.out.println("La cantidad de argumentos es erronea");
		
		else 
			if(args.length == 1){
			
				archivo=args[0];

				analizadorLexico2 analizador = new analizadorLexico2(archivo);
				Token t;
				
				
				try {
					
					do{	

						t = analizador.getToken();
						
						System.out.println("Nombre token: "		+ t.getNombre().toString()+ 
											" | Lexema: "		+ t.getLexema().toString() + 
											" | Nro linea: "	+ t.getNroLinea() + 
											" | Nro columna: "	+ t.getNroColumna());
						
						System.out.println("------------------------------------------------------------------------------------");
					
					} while(!t.getNombre().equals("EOF"));
				
					
				} catch (Exception e) { 
					System.out.println(e.getMessage());
				}		
			}	
	
			else 
				if (args.length ==2){

						archivo=args[0];
						analizadorLexico analizador = new analizadorLexico(archivo);
						Token t;
						String archivoS = args[1];
		    			File file = new File(archivoS);
		    			PrintStream salida;
						
		    			try {
							salida = new PrintStream(file);					
							System.setOut(salida);
						} catch (FileNotFoundException e1) {
							System.out.println("No se encontro el archivo");
						}
		    			try {
							
							do{	

								t = analizador.getToken();
								
								System.out.println("Nombre token: "		+ t.getNombre().toString()+ 
													" | Lexema: "		+ t.getLexema().toString() + 
													" | Nro linea: "	+ t.getNroLinea() + 
													" | Nro columna: "	+ t.getNroColumna());
								
								System.out.println("------------------------------------------------------------------------------------");
							
							} while(!t.getNombre().equals("EOF"));
						
							
						} catch (Exception e) { 
							System.out.println(e.getMessage());
						}		
			
				}
		
					
				//	long endTime= System.nanoTime();
				//	System.out.println("took" + (endTime - startTime));
		}	
	}
		
	