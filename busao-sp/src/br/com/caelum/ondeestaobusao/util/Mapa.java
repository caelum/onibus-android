package br.com.caelum.ondeestaobusao.util;

import android.app.Activity;
import br.com.caelum.ondeestaobusao.model.Coordenada;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class Mapa {
	private MapView mapa;
	
	public Mapa(Activity activity, MapView mapa) {
		this.mapa = mapa;
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);

		mapa.getController().setZoom(17);
	}
	
	public void adicionaCamada(Overlay overlay) {
		this.mapa.getOverlays().add(overlay);
	}
	
	public void centralizaNa(Coordenada coordenada) {
		mapa.getController().setCenter(coordenada.toGeoPoint());
		mapa.getController().animateTo(coordenada.toGeoPoint());
	}
	
	public void redesenha() {
		mapa.invalidate();
	}
	
	public MapView getMapView() {
		return mapa;
	}

	public void limpa() {
		mapa.getOverlays().clear();
	}
}
