package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.gps.GPSObserver;
import br.com.caelum.ondeestaobusao.map.PontoDoOnibusOverlay;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapaDoOnibusFragment extends Fragment implements GPSObserver, AsyncResultDelegate<List<Ponto>> {

	private MapView mapa;
	private GPSControl gps;
	private BusaoActivity activity;
	private final Onibus onibus;
	private PontoDoOnibusOverlay pontoOverlay;
	private List<Overlay> overlays;
	private ViewGroup container;
	private AsyncTask<Long, Void, List<Ponto>> pontosDoOnibusTask;

	public MapaDoOnibusFragment(GPSControl gps, Onibus onibus) {
		this.gps = gps;
		this.onibus = onibus;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
		this.activity = (BusaoActivity) inflater.getContext();
		this.activity.atualizaNomeFragment(onibus.toString());

		container = this.activity.getMapViewContainer();
		mapa = this.activity.getMapView();

		configuraMapView();

		this.gps.registerObserver(this);

		return container;
	}

	private void configuraMapView() {
		mapa = (MapView) container.findViewById(R.id.map_view);
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);

		mapa.getController().setZoom(17);
		pontoOverlay = new PontoDoOnibusOverlay(activity);
		overlays = mapa.getOverlays();
	}

	@Override
	public void callback(Coordenada coordenada) {
		mapa.getController().setCenter(coordenada.toGeoPoint());
		mapa.getController().animateTo(coordenada.toGeoPoint());
		
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(activity, mapa);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();
		overlays.add(myLocationOverlay);

		activity.exibeProgress();
		activity.atualizaTextoDoProgress(R.string.buscando_pontos_onibus);
		pontosDoOnibusTask = new PontosDoOnibusTask(this).execute(onibus.getId());
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		pontoOverlay.clear();

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
	public void onDestroyView() {
		super.onDestroyView();
		
		if (pontosDoOnibusTask != null && Status.RUNNING.equals(pontosDoOnibusTask.getStatus())) {
			pontosDoOnibusTask.cancel(true);
		}
		
		overlays.clear();
		pontoOverlay.clear();
		
		ViewGroup parentViewGroup = (ViewGroup) container.getParent();
		if (null != parentViewGroup) {
			parentViewGroup.removeView(container);
		}
	}

}
