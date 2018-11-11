// Incompatibilidad de tipos en una asignacion

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		B b;
		b = 1;
	}

}

class B {

}