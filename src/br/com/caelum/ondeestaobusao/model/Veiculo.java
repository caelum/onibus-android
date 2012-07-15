package br.com.caelum.ondeestaobusao.model;

import com.google.android.maps.OverlayItem;

public class Veiculo {
	private Localizacao localizacao;
	private boolean para_deficiente;
	
	public Localizacao getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}
	public boolean isPara_deficiente() {
		return para_deficiente;
	}
	public void setPara_deficiente(boolean para_deficiente) {
		this.para_deficiente = para_deficiente;
	}
	
	public OverlayItem toOverlayItem() {
		return new OverlayItem(
				getLocalizacao().toGeoPoint(), "Localização do ponto:", "");
	}
}
