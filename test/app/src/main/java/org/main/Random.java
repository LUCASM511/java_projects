package org.main;

public class Random {
	private static String[] alfa = {"A", "B", "C"};

	public Random() {

	}

	// Gerenate random numbers
	public static int generateRandom() {
		int oneLenght = alfa.length;
		int rand1 = (int) (Math.random() * oneLenght);
		int rand2 = (int) (Math.random() * 30.5);

		int rand3 = (int) (Math.random() * rand1 * rand2);

		return rand3;
	}
}
