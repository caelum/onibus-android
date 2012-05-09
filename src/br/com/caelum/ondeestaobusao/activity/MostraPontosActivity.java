package br.com.caelum.ondeestaobusao.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.OverlayItemConverter;
import br.com.caelum.ondeestaobusao.map.PontoClickOverlay;
import br.com.caelum.ondeestaobusao.map.PontoOverlay;
import br.com.caelum.ondeestaobusao.map.PontoOverlayItem;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.GetJsonAsyncTask;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MostraPontosActivity extends MapActivity implements AsyncResultDelegate<List<Ponto>> {

	private MapView mapView;
	private List<Overlay> overlays;
	private MapController mapController;
	private PontoClickOverlay pontoOverlay;
	private PontoOverlay meuPontoOverlay;
	private View atualizaMapa;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.pontos_map);

		Coordenada minhaLocalizacao = (Coordenada) getIntent().getSerializableExtra(Extras.LOCALIZACAO);

		inicializaAtributos();

		meuPontoOverlay = new PontoOverlay(getResources().getDrawable(R.drawable.ic_pin_current), this);

		mapView.displayZoomControls(true);
		mapView.setBuiltInZoomControls(true);

		mapController.setZoom(17);
		atualizaLocalizacaoPontos(minhaLocalizacao);
	}

	private void atualizaLocalizacaoPontos(Coordenada minhaLocalizacao) {
		View progressBar = findViewById(R.id.progress_bar);
		progressBar.setVisibility(View.VISIBLE);

		mapView.setClickable(false);
		
		meuPontoOverlay.clear();
		meuPontoOverlay.addOverlay(new OverlayItemConverter(getBaseContext()).convert(minhaLocalizacao));
		
		mapController.setCenter(minhaLocalizacao.toGeoPoint());
		mapController.animateTo(minhaLocalizacao.toGeoPoint());

		new GetJsonAsyncTask<Coordenada, List<Ponto>>(new PontosEOnibusTask(this)).execute(minhaLocalizacao);
	}

	private void inicializaAtributos() {
		atualizaMapa = findViewById(R.id.atualiza_mapa);

		mapView = (MapView) findViewById(R.id.map_view);
		overlays = mapView.getOverlays();
		mapController = mapView.getController();

		pontoOverlay = new PontoClickOverlay(this.getResources().getDrawable(R.drawable.ic_pin_bus), this);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		overlays.clear();
		pontoOverlay.clear();

		for (Ponto ponto : pontos) {
			pontoOverlay.addOverlay(new PontoOverlayItem(ponto));
		}

		overlays.add(pontoOverlay);
		overlays.add(meuPontoOverlay);

		mapView.invalidate();
		mapView.setVisibility(View.VISIBLE);
		mapView.setClickable(true);
		
		atualizaMapa.setVisibility(View.VISIBLE);
		findViewById(R.id.progress_bar).setVisibility(View.GONE);
	}

	public void atualizarMapa(View v) {
		v.setVisibility(View.GONE);
		GeoPoint p = mapView.getMapCenter();
		atualizaLocalizacaoPontos(new Coordenada(p));
	}

	@Override
	public void dealWithError() {
		new AlertDialogBuilder(this).build().show();		
	}

}
