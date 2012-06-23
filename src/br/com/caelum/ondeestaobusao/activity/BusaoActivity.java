package br.com.caelum.ondeestaobusao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.fragments.MapaComPontosEOnibusesFragment;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosFragment;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.google.android.maps.MapView;

public class BusaoActivity extends FragmentActivity {

	private GPSControl gps;
	private TextView textProgressBar;
	private View progressBar;
	private ViewGroup mapViewContainer;
	private MapView mapView;
	private PontosProximosFragment pontosProximosFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		carregaElementosDaTela();

		gps = new GPSControl(this);
		gps.execute();

		AppRater.app_launched(this);
		
		mapViewContainer = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.mapa, null);
		mapView = (MapView) mapViewContainer.findViewById(R.id.map_view);

		pontosProximosFragment = new PontosProximosFragment(gps);
		
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_main, pontosProximosFragment, pontosProximosFragment.getClass().getName()).commit();
		
		gps.registerObserver(pontosProximosFragment);
	}

	private void carregaElementosDaTela() {
		progressBar = findViewById(R.id.progress_bar);
		textProgressBar = (TextView) findViewById(R.id.progress_text);
	}

	@Override
	public void finish() {
		gps.shutdown();
		super.finish();
	}
	
	public void atualizaTextoDoProgress(int string) {
		textProgressBar.setText(getResources().getString(string));
	}

	public void escondeProgress() {
		progressBar.setVisibility(View.GONE);
	}

	public void exibeProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void atualiza(View v) {
		gps.execute();
		exibeProgress();
	}
	
//	@Override
//	protected boolean isRouteDisplayed() {
//		return false;
//	}

	public void dealWithError() {
		new AlertDialogBuilder(this).build().show();
	}

	public MapView getMapView() {
		return mapView;
	}
	
	public ViewGroup getMapViewContainer() {
		return mapViewContainer;
	}
	
	public GPSControl getGps() {
		return gps;
	}
	
	public void onClickMap(View v) {
		MapaComPontosEOnibusesFragment mapaComPontosEOnibusesFragment = (MapaComPontosEOnibusesFragment) getSupportFragmentManager().findFragmentByTag(MapaComPontosEOnibusesFragment.class.getName());
		if (mapaComPontosEOnibusesFragment == null) {
			mapaComPontosEOnibusesFragment = new MapaComPontosEOnibusesFragment(this, this.pontosProximosFragment.getPontos());
		}
		
		this.pontosProximosFragment.vaiPara(mapaComPontosEOnibusesFragment, MapaComPontosEOnibusesFragment.class.getName());
	}
}
