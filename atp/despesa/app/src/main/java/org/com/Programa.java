package org.com;

import java.util.Scanner;

public class Programa {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		Despesa despesa[] = new Despesa[3];

		despesa[0] = new Despesa();

		despesa[0].setDados(23000);

		System.out.println(despesa.toString());

		// System.out.println("Janeiro" + despesa[0].display());

		System.out.println("Nome => ");
		String input =  scan.next();

	}

}
