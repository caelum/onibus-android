package br.com.caelum.ondeestaobusao.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.PontoOverlay;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.GetJsonAsyncTask;
import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MostraItinerarioActivity extends MapActivity implements AsyncResultDelegate<List<Ponto>> {

	private MapView mapView;
	private List<Overlay> overlays;
	private TextView titulo;
	private MyLocationOverlay myLocationOverlay;
	private MapController mapController;
	private PontoOverlay pontoOverlay;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.mapa);

		Coordenada minhaLocalizacao = (Coordenada) getIntent().getSerializableExtra(Extras.LOCALIZACAO);
		Onibus onibus = (Onibus) getIntent().getSerializableExtra(Extras.ONIBUS);

		inicializaAtributos();

		titulo.setText(onibus.getNome());

		mapView.displayZoomControls(true);
		mapView.setBuiltInZoomControls(true);

		overlays.add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		myLocationOverlay.enableMyLocation();

		mapController.setZoom(17);
		mapController.setCenter(minhaLocalizacao.toGeoPoint());
		mapController.animateTo(minhaLocalizacao.toGeoPoint());

		new GetJsonAsyncTask<Long, List<Ponto>>(new PontosDoOnibusTask(this)).execute(onibus.getId());
	}

	private void inicializaAtributos() {
		titulo = (TextView) findViewById(R.id.titulo_mapa);
		mapView = (MapView) findViewById(R.id.map_view);
		overlays = mapView.getOverlays();
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapController = mapView.getController();

		pontoOverlay = new PontoOverlay(this.getResources().getDrawable(R.drawable.ic_bus_stop), this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		overlays.clear();
		pontoOverlay.clear();
		overlays.add(myLocationOverlay);

		for (Ponto ponto : pontos) {
			pontoOverlay.addOverlay(new OverlayItem(ponto.getCoordenada().toGeoPoint(), "Localização do ponto:", ponto
					.getDescricao()));
		}
		overlays.add(pontoOverlay);

		mapView.invalidate();

		mapView.setVisibility(View.VISIBLE);
		findViewById(R.id.progress_bar).setVisibility(View.GONE);
	}

	@Override
	public void dealWithError() {
		new AlertDialogBuilder(this).build().show();		
	}
}
