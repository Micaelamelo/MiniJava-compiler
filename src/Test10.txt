// Variable local declarada solo en un if

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		if (true) {
			int a;
		}
		a = 1;
	}

}