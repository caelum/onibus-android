package br.com.caelum.ondeestaobusao.model;

import com.google.android.maps.OverlayItem;

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
	
	public OverlayItem toOverlayItem(String endereco) {
		return new OverlayItem(
				getLocalizacao().toGeoPoint(), "Localização do ônibus:", endereco);
	}
}
