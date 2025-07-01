package org;

/**
 * Chamado - armazena o ID, DESCRIÇÂO e PRIORIDADE do problema
 */
public class Chamado {
	private int id;
	private String descricao;
	private int prioridade;

	/**
	 * Construtor 1 sem parâmetro
	 **/
	public Chamado() {
		this.id = 0;
		this.descricao = " ";
		this.prioridade = 0;
	}
	
	/**
	 * Construtor 2 com ID
	 **/
	public Chamado(int id) {
		this.id = id;
		this.descricao = " ";
		this.prioridade = 0;
	}

	/**
	 * Construtor 3 com ID, DESCRICAO
	 **/
	public Chamado(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
		this.prioridade = 0;
	}

	/**
	 * Construtor 4 com ID, DESCRICAO, PRIORIDADE
	 **/
	public Chamado(int id, String descricao, int prioridade) {
		this.id = id;
		this.descricao = descricao;
		this.prioridade = prioridade;
	}
}
