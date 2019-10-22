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
						
						archivo= args[0];
						// archivo = "C:\\Users\\miksm\\workspace\\etapa 5\\test.txt";
						String salida = "salida2.txt";
						analizadorSintacticoSemantico2 analizador = new analizadorSintacticoSemantico2(archivo, salida);
						System.out.println("Es sintactica y semanticamente correcto");
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
	
				}
			
			else if (args.length==2){
				
					String archivoIN = args[0];
					String salidaOUT = args[1];
					try {
						analizadorSintacticoSemantico2 analizador = new analizadorSintacticoSemantico2(archivoIN, salidaOUT);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
						
				//	long endTime= System.nanoTime();
				//	System.out.println("took" + (endTime - startTime));
		
}
	