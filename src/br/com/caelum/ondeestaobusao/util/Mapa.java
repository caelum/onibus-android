package br.com.caelum.ondeestaobusao.util;



import android.view.LayoutInflater;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.model.Coordenada;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class Mapa {
	private ViewGroup mapViewContainer;
	private MapView mapa;
	private final BusaoActivity activity;
	private MyLocationOverlay myLocationOverlay;
	
	public Mapa(BusaoActivity activity) {
		this.activity = activity;

		mapViewContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.mapa, null);
		mapa = (MapView) mapViewContainer.findViewById(R.id.map_view);
		
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);

		mapa.getController().setZoom(17);
	}
	
	public void habilitaBussula() {
		myLocationOverlay = new MyLocationOverlay(activity, mapa);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		mapa.getOverlays().add(myLocationOverlay);
	}
	
	public void desabilitaBussula() {
		if (myLocationOverlay != null) {
			myLocationOverlay.disableMyLocation();
		}
	}
	
	public void adicionaCamada(Overlay overlay) {
		this.mapa.getOverlays().add(overlay);
	}
	
	public void centralizaNa(Coordenada coordenada) {
		mapa.getController().setCenter(coordenada.toGeoPoint());
		mapa.getController().animateTo(coordenada.toGeoPoint());
	}
	
	public ViewGroup getMapViewContainer() {
		return mapViewContainer;
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

	public void removeDaTela() {
		ViewGroup parentViewGroup = (ViewGroup) mapViewContainer.getParent();
		if (null != parentViewGroup) {
			parentViewGroup.removeView(mapViewContainer);
		}
	}
}
