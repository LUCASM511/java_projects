package org;

public class Aluno {
	private String nome;
	private int idade;
	private String matricula;

	public Aluno(String nome, int idade, String matricula) {
		this.nome = nome;
		this.idade = idade;
		this.matricula = matricula;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public int getIdade() {
		return idade;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getMatricula() {
		return matricula;
	}

	public boolean verifica_idade(int idade) {
		if (idade >= 18)
			return true;
		return false;
	}

}
