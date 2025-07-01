package org.program;

// Classe Account que contém uma variável de instância name
// e métodos para configurar e obter seu valor
public class Account {
	private String name;
	
	public Account() {}

	// construtor inicializa name com nome do parâmetro
	public Account(String name) {
		this.name = name;
	}
 
	// método para definir o nome do objeto
	public void setName(String name) {

		this.name = name; // armazena o nome
	}
	
	// método para recuperar o nome do objeto
	public String getName() {
		return name; // retorna valor do nome para o chamador
	}
}
