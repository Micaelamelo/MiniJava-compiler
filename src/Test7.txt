// Incompatibilidad de tipos en una asignacion

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		A a;
		a = new A();
		B b;
		b = new B();
		a = b;
	}

}

class B {

}