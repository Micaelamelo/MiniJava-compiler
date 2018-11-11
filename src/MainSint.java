public class MainSint {

	public static void main(String args[]) {

	//	long startTime=System.nanoTime();
String archivo;
		
		if(args.length == 0)
			System.out.println("La cantidad de argumentos es erronea");
		
		else 
			if(args.length == 1){
				archivo=args[0];

				try {
				//	String archivo = "C:\\Users\\miksm\\workspace\\ALSYG\\src\\Test1.txt";
					String nombreArchivo= args[0];
					String salida = "salida.txt";
					analizadorSintacticoSemantico2 analizador = new analizadorSintacticoSemantico2(archivo, salida);
					System.out.println("Es sintactica y semanticamente correcto");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			}	
						
				//	long endTime= System.nanoTime();
				//	System.out.println("took" + (endTime - startTime));
		}	
	}