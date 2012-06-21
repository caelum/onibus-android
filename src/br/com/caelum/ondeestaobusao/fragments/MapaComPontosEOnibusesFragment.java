package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.R.id;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.PontoComOnibusesOverlay;
import br.com.caelum.ondeestaobusao.map.PontoOverlayItem;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapaComPontosEOnibusesFragment extends GPSFragment implements AsyncResultDelegate<List<Ponto>> {
	private MapView mapa;
	private BusaoActivity activity;
	private List<Ponto> pontos;
	private List<Overlay> overlays;
	private ViewGroup container;
	private Coordenada coordenada;
	private PontoComOnibusesOverlay pontoComOnibusesOverlay;

	public MapaComPontosEOnibusesFragment(BusaoActivity activity, List<Ponto> pontos) {
		super(activity.getGps());

		this.activity = activity;
		container = activity.getMapViewContainer();
		mapa = activity.getMapView();
		this.pontos = pontos;
		pontoComOnibusesOverlay = new PontoComOnibusesOverlay(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		return container;
	}

	private void configuraMapView() {
		mapa = (MapView) container.findViewById(R.id.map_view);
		mapa.displayZoomControls(true);
		mapa.setBuiltInZoomControls(true);

		mapa.getController().setCenter(coordenada.toGeoPoint());
		mapa.getController().animateTo(coordenada.toGeoPoint());
		mapa.getController().setZoom(17);
		overlays = mapa.getOverlays();
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		for (Ponto ponto : pontos) {
			pontoComOnibusesOverlay.addOverlay(new PontoOverlayItem(ponto));
		}
		
		overlays.add(pontoComOnibusesOverlay);
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

		overlays.clear();

		ViewGroup parentViewGroup = (ViewGroup) container.getParent();
		if (null != parentViewGroup) {
			parentViewGroup.removeView(container);
		}
	}

	@Override
	public void updateHeader(View view) {
		TextView titulo = (TextView) view.findViewById(id.fragment_name);
		titulo.setText(getActivity().getResources().getString(R.string.procure_no_mapa));
	}

	@Override
	public void callback(Coordenada coordenada) {
		this.coordenada = coordenada;
		configuraMapView();
		dealWithResult(pontos);
	}
}
