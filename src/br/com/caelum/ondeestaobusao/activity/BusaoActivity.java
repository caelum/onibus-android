package br.com.caelum.ondeestaobusao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.fragments.PontosProximos;
import br.com.caelum.ondeestaobusao.generic.fragment.GenericFragmentManager;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.widget.AppRater;

public class BusaoActivity extends Activity {
	
	private GPSControl gps;
	private TextView textProgressBar;
	private View frameLayout;
	private View progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		carregaElementosDaTela();

		gps = new GPSControl(this);
		gps.execute();
		
		AppRater.app_launched(this);
		
		new GenericFragmentManager(this).replace(R.id.fragment_main, new PontosProximos(gps));
		
	}

	private void carregaElementosDaTela() {
		frameLayout = findViewById(R.id.fragment_main);
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
		frameLayout.setVisibility(View.VISIBLE);
	}
	
	public void exibeProgress() {
		progressBar.setVisibility(View.VISIBLE);
		frameLayout.setVisibility(View.GONE);
	}
	
	public void atualiza(View v) {
		gps.execute();
		exibeProgress();
	}

}
