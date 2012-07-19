package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;

public class Sentido implements Serializable {
	private int codigo;
	private String terminalPartida;
	private String terminalSecundario;
	private boolean circular;

	public Sentido(int codigo, String terminalPartida, String terminalSecundario, boolean circular) {
		this.codigo = codigo;
		this.terminalPartida = terminalPartida;
		this.terminalSecundario = terminalSecundario;
		this.circular = circular;
	}

	public boolean isCircular() {
		return circular;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getTerminalPartida() {
		return terminalPartida;
	}

	public String getTerminalSecundario() {
		return terminalSecundario;
	}

	@Override
	public String toString() {
		return this.getCodigo() == 1 ? this.getTerminalPartida() : this.getTerminalSecundario();
	}

}
