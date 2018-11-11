// Declaracion de variable local repetida en un if

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		int a;
		if (true) {
			int a;
		}
	}

}