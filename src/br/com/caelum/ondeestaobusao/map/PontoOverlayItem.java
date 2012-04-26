package br.com.caelum.ondeestaobusao.map;

import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PontoOverlayItem extends OverlayItem {

	private Ponto ponto;

	public PontoOverlayItem(GeoPoint geoPoint, String title, String descricao) {
		super(geoPoint, title, descricao);
	}

	public PontoOverlayItem(Ponto ponto) {
		super(ponto.getCoordenada().toGeoPoint(), null, null);
		this.ponto = ponto;
	}
	
	
	public Ponto getPonto() {
		return ponto;
	}
}
