// Sentencia de retorno solo en el else del if

class Main {

	static void main() {}

}

class A {


	dynamic int m1() {
		int c;
		c = 1;
		if (true) {
			c = 2;
		} else {
			return c;
		}
	}

}