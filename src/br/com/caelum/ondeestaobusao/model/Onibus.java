package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Onibus implements Serializable {
	private final long id;
	private final String nome;
	private final String linha;

	public Onibus(long id, String linha, String nome) {
		this.id = id;
		this.linha = linha;
		this.nome = nome;
	}
	
	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getLinha() {
		return linha;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}
	
}
