package org.apostilha;

class ApostilhaTest {
	public static void main(String []args) {
		String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXZ";

		System.out.println("This string is: " + alfabeto);
		System.out.println("Length: " + alfabeto.length());

    System.out.prin
		System.out.println("The character at index 5: " + alfabeto.charAt(5));
		System.out.println("The index of character Z is: " + alfabeto.indexOf('Z'));

		String[] cars = {"Volvo", "BMW", "Ferrari"};
		for (int i = 0; i < cars.length; i++) {
			System.out.println(cars[i]);
		}

		// for-each loop
		System.out.println("For-each loop");
		for (String i: cars) {
			System.out.println(i);
		}
	}
}
