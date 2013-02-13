package br.com.caelum.ondeestaobusao.model;


public class Veiculo {
	private Coordenada localizacao;
	private boolean deficientes;
	
	public Coordenada getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(Coordenada localizacao) {
		this.localizacao = localizacao;
	}
	public boolean isDeficientes() {
		return deficientes;
	}
	public void setDeficientes(boolean para_deficiente) {
		this.deficientes = para_deficiente;
	}
}
