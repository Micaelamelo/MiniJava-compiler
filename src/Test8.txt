// Incompatibilidad de tipos en una asignacion

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		A a;
		a = this;
		String b;
		b = a;
	}

}