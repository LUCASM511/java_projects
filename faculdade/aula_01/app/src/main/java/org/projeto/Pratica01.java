package org.projeto;

//Java Program to create a showInputDialog in JOptionPane
import javax.swing.JOptionPane;

public class Pratica01 {
    public static void main(String[] args) {
        // Prompt the user to enter their article name and store it in the 'name'
        // variable
        String name = JOptionPane.showInputDialog("Enter your Article Name:");

        // Create a JOptionPane to display a message with a personalized greeting
        JOptionPane.showMessageDialog(null, "GFG " + name + "!");
    }
}
