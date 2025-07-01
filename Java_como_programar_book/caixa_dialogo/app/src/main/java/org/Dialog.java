package org;

import javax.swing.JOptionPane;

public class Dialog {
	public static void main(String[] args) {

		// Take user input
		String name = JOptionPane.showInputDialog(null, "What is your name? ");

		String message = String.format("Welcome, %s, to Java Programming", name);

		// prompt the message for the user
		JOptionPane.showMessageDialog(null, message);
	}
}
