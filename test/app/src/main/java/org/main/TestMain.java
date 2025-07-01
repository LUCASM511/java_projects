package org.main;

// import java.util.Scanner;

public class TestMain {
	public static void main(String []args) {

		MusicPlay musicPlay =  new MusicPlay();

		// System.out.println(musicPlay.choiceGender(1));
		int op = 1;

		System.out.println(musicPlay.musicToPlay("Flora Cash"));
		musicPlay.musicStop();

		// Random random = new Random();
		// Animals animals = new Animals("Felina", 20);
		//
		// System.out.println("Random number: " + Random.generateRandom());
		//
		// animals.setEspecie("Lion");
		// animals.setAge(37);
		//
		// System.out.println(animals.toString());
		//
		// // For-Each loop
		//
		// String something[] = {"Rice", "Kudasai"};
		//
		// for (String i: something) {
		// 	System.out.println(i);
		// }
		//
		// // Nested Loop
		// for (int i = 0; i < 3; i++) {
		// 	System.out.println("\tOuterLoop " + i);
		// 	for (int j = 0; j < 2; j++) {
		// 		System.out.println("InnerLoop " + j);
		// 	}
		// }
		//
		// // Multidimensional arrays
		// int arrayMult[][] = { {1, 2, 3}, {4, 5, 6} };
		// int[][] arrayMult2 = { {1, 2, 3}, {4, 5, 6} };
		//
		// System.out.println(arrayMult[1][2]); // print 6
		// System.out.println(arrayMult2[0][1]); // print 2
		//
		// System.out.println("\nMult Nested");
		// for (int i = 0; i < arrayMult.length; ++i) {
		// 	for (int j = 0; j < arrayMult[i].length; ++j) {
		// 		System.out.println(arrayMult[i][j]);
		// 	}
		// }
		//
		// System.out.println("\nFor each");
		// for (int[] i: arrayMult) {
		// 	for (int j : i){
		// 		System.out.println(j);
		// 	}
		// }
		//
		// // int array[] = {1, 3, 4, 5};
		// // String[] alfa = new String[] {"A", "B"};
		// //
		// // System.out.println(alfa[0]);
		//
		// for (int i = 0; i < array.length ; i++) 
		// 	System.out.println(array[i]);
		//
		// // casting
		// double num = 10.9;
		// int num2 = (int) num;
		//
		// int maxScore = 500;
		// int userScore = 323;
		//
		// /* Calculate the percantage of the user's score in relation to the maximum available score.
		// Convert userScore to float to make sure that the division is accurate */
		// float percentage = (float) userScore / maxScore * 100.0f;
		//
		// System.out.println("User percentage is " + percentage);
		//
		// Scanner scanner = new Scanner(System.in);
		//
		// System.out.println("Type our name: ");
		// String input = scanner.nextLine();
		//
		// scanner.close();
		//
		// System.out.println(input);
	}

}
