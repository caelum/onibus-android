package br.com.caelum.ondeestaobusao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosFragment;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.google.android.maps.MapView;

public class BusaoActivity extends FragmentActivity {

	private GPSControl gps;
	private TextView textProgressBar;
	private View progressBar;
	private TextView fragmentName;
	private ViewGroup mapViewContainer;
	private MapView mapView;

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

		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_main, new PontosProximosFragment(gps), "Pontos Próximos").commit();
	}

	private void carregaElementosDaTela() {
		progressBar = findViewById(R.id.progress_bar);
		textProgressBar = (TextView) findViewById(R.id.progress_text);
		fragmentName = (TextView) findViewById(R.id.fragment_name);
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

	public void atualizaNomeFragment(String name) {
		this.fragmentName.setText(name);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void dealWithError() {
		new AlertDialogBuilder(this).build().show();
	}

	public MapView getMapView() {
		return mapView;
	}
	
	public ViewGroup getMapViewContainer() {
		return mapViewContainer;
	}
}
