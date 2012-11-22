package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Onibus implements Serializable {
	private Long id;
	private String letreiro;
	private int codigoGPS;
	private Operacao operacao;
	private Sentido sentido;

	public Onibus(Long id, String letreiro, int codigoGPS, Operacao operacao, Sentido sentido) {
		this.id = id;
		this.letreiro = letreiro;
		this.codigoGPS = codigoGPS;
		this.operacao = operacao;
		this.sentido = sentido;
	}

	public Long getId() {
		return id;
	}

	public String getLetreiro() {
		return letreiro;
	}

	public Operacao getOperacao() {
		return operacao;
	}

	public Sentido getSentido() {
		return sentido;
	}

	public int getCodigoGPS() {
		return codigoGPS;
	}

	public String toString() {
		return this.letreiro + " - " + this.sentido;
	}

}
