package br.com.caelum.ondeestaobusao.widgets.actionbar;

import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.activity.MapaDoOnibusActivity;
import br.com.caelum.ondeestaobusao.util.Mapa;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class BusaoNoMapaListener implements TabListener {
	private Mapa mapa;
	private MapaDoOnibusActivity activity;

	public BusaoNoMapaListener(MapaDoOnibusActivity activity, Mapa mapa) {
		this.activity = activity;
		this.mapa = mapa;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mapa.limpa();
		if (tab.getTag().equals(BusaoNoMapa.ITINERARIO)) {
			activity.exibePontosNoMapa();
		} else {
			activity.exibeVeiculosNoMapa();
		}
		mapa.redesenha();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}


}
