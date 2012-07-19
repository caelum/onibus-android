package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;

public class Operacao implements Serializable{
	private String diaUtil;
	private String sabado;
	private String domingo;
	private int intervalo;
	
	public Operacao(String diaUtil, String sabado, String domingo, int intervalo) {
		this.diaUtil = diaUtil;
		this.sabado = sabado;
		this.domingo = domingo;
		this.intervalo = intervalo;
	}
	
	public String getDiaUtil() {
		return diaUtil;
	}
	
	public String getSabado() {
		return sabado;
	}
	
	public String getDomingo() {
		return domingo;
	}
	
	public int getIntervalo() {
		return intervalo;
	}
	
}
