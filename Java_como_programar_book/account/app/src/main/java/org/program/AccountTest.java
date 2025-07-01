package org.program;

import java.util.Scanner;

public class AccountTest {
	public static void main(String[] args) {
		// cria um objeto Scanner para obter entrada a partir d
		// da janel de comando
		Scanner input = new Scanner(System.in);
	
		// cria um objeto Account e o atribui a myAccount
		Account myAccount = new Account();
		
		// exibe o valor inicial do name (null)
		System.out.printf("Initial name is: %s%n%n", myAccount.getName());

		System.out.println("Please enter the name:");
		String theName = input.nextLine(); // lê uma linha de texto
		myAccount.setName(theName); // insere theName em myAccount
		System.out.println();

		// exibe o nome armazenado no objeto myAccount
		System.out.printf("Name in object myAccount is: %n%s%n", myAccount.getName());

		input.close(); // fecha o scanner

		Account account1 = new Account("Lord Valdermord");
	}
}
