package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.R.id;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.PontoDoOnibusOverlay;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapaDoOnibusFragment extends GPSFragment implements AsyncResultDelegate<List<Ponto>> {

	private MapView mapa;
	private BusaoActivity activity;
	private Onibus onibus;
	private PontoDoOnibusOverlay pontoOverlay;
	private List<Overlay> overlays;
	private ViewGroup container;
	private AsyncTask<Long, Void, List<Ponto>> pontosDoOnibusTask;
	
	public MapaDoOnibusFragment(BusaoActivity activity) {
		super(activity.getGps());
		
		this.activity = activity;
		container = activity.getMapViewContainer();
		mapa = activity.getMapView();

		configuraMapView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		this.onibus = (Onibus) getArguments().getSerializable(Extras.ONIBUS);
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
			pontoOverlay.addOverlay(ponto.toOverlayItem());
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

	@Override
	public void updateHeader(View view) {
		TextView titulo = (TextView) view.findViewById(id.fragment_name);
		titulo.setText(onibus.toString());
	}

}
