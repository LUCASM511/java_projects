package org;

import javax.swing.JOptionPane;

public class DialogSum { // SUM OF TWO NUMBER TAKEN FROM THE USER INPUT
	public static void main(String[] args) {

		// take the first number
		String sum = JOptionPane.showInputDialog("Enter with the first number: ");

		// take the second number
		String sum1 = JOptionPane.showInputDialog("Enter with the second number: ");

		// convert the numbers to int and sum
		int result = (Integer.parseInt(sum) + Integer.parseInt(sum1));

		// prompt out the sum of numbers taken before
		JOptionPane.showMessageDialog(null, result);
	}
}
