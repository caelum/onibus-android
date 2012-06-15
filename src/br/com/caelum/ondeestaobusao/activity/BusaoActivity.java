package br.com.caelum.ondeestaobusao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosFragment;
import br.com.caelum.ondeestaobusao.generic.fragment.GenericFragmentManager;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.google.android.maps.MapActivity;

public class BusaoActivity extends MapActivity {
	
	private GPSControl gps;
	private TextView textProgressBar;
	private View progressBar;
	private GenericFragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		carregaElementosDaTela();

		gps = new GPSControl(this);
		gps.execute();
		
		AppRater.app_launched(this);
		
		fragmentManager = new GenericFragmentManager(this).replace(R.id.fragment_main, new PontosProximosFragment(gps));
		
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
	
	public void exibeProgressEEscondeFrameLayout() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void exibeProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void atualiza(View v) {
		gps.execute();
		exibeProgressEEscondeFrameLayout();
	}
	
	public GenericFragmentManager getGenericFragmentManager() {
		return fragmentManager;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void dealWithError() {
		new AlertDialogBuilder(this).build().show();		
	}

}
