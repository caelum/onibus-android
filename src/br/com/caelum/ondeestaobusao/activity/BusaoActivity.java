package br.com.caelum.ondeestaobusao.activity;

import android.app.Activity;
import android.os.Bundle;
import br.com.caelum.ondeestaobusao.fragments.PontosProximos;
import br.com.caelum.ondeestaobusao.generic.fragment.GenericFragmentManager;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.widget.AppRater;

public class BusaoActivity extends Activity {
	
	private GPSControl gps;
	private Coordenada localizacaoAtual;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		gps = new GPSControl(this);
		gps.execute();
		
		AppRater.app_launched(this);
		
		new GenericFragmentManager(this).replace(R.id.fragment_main, new PontosProximos(gps));
		
	}

	@Override
	public void finish() {
		gps.shutdown();
		super.finish();
		
	}

	public void setAtual(Coordenada localizacaoAtual) {
		this.localizacaoAtual = localizacaoAtual;
	}

}
