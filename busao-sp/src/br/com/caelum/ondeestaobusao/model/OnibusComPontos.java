package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;
import java.util.List;

public class OnibusComPontos implements Serializable {
	private Onibus onibus;
	private List<Ponto> pontos;
	
	private OnibusComPontos(Onibus onibus, List<Ponto> pontos) {
		this.onibus = onibus;
		this.pontos = pontos;
	}
	
	public Onibus getOnibus() {
		return onibus;
	}
	
	public List<Ponto> getPontos() {
		return pontos;
	}
}
