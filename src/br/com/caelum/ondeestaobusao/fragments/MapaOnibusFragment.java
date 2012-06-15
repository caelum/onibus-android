package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.generic.fragment.GenericFragment;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.gps.GPSObserver;
import br.com.caelum.ondeestaobusao.map.PontoOverlay;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapaOnibusFragment implements GenericFragment, GPSObserver, AsyncResultDelegate<List<Ponto>> {

	private MapView mapa;
	private GPSControl gps;
	private BusaoActivity activity;
	private final Onibus onibus;
	private PontoOverlay pontoOverlay;
	private List<Overlay> overlays;

	public MapaOnibusFragment(GPSControl gps, Onibus onibus) {
		this.gps = gps;
		this.onibus = onibus;
	}

	@Override
	public View onCreateView(Activity activity, ViewGroup container) {
		this.activity = (BusaoActivity) activity;

		mapa = (MapView) activity.getLayoutInflater().inflate(R.layout.mapa, container, false);
		configuraMapView();
		
		this.gps.registerObserver(this);
		return mapa;
	}

	private void configuraMapView() {
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);

		mapa.getController().setZoom(17);
		pontoOverlay = new PontoOverlay(activity, R.drawable.ic_bus_stop);
		overlays = mapa.getOverlays();
	}

	@Override
	public void finish() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void callback(Coordenada coordenada) {
		mapa.getController().setCenter(coordenada.toGeoPoint());
		mapa.getController().animateTo(coordenada.toGeoPoint());

		activity.exibeProgress();
		activity.atualizaTextoDoProgress(R.string.buscando_pontos_onibus);
		new PontosDoOnibusTask(this).execute(onibus.getId());
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		overlays.clear();
		pontoOverlay.clear();

		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(activity, mapa);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		
		overlays.add(myLocationOverlay);
		for (Ponto ponto : pontos) {
			pontoOverlay.addOverlay(new OverlayItem(ponto.getCoordenada().toGeoPoint(), "Localização do ponto:", ponto
					.getDescricao()));
		}
		overlays.add(pontoOverlay);

		mapa.invalidate();
		activity.escondeProgress();
	}

	@Override
	public void dealWithError() {
		activity.dealWithError();
	}

	@Override
	public String getName() {
		return onibus.getLetreiro();
	}

}
