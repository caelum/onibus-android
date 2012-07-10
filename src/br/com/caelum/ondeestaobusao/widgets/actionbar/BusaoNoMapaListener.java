package br.com.caelum.ondeestaobusao.widgets.actionbar;

import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.fragments.Mapa;
import br.com.caelum.ondeestaobusao.fragments.MapaDoOnibusFragment;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class BusaoNoMapaListener implements TabListener {
	private final Mapa mapa;
	private final MapaDoOnibusFragment fragment;

	public BusaoNoMapaListener(Mapa mapa, MapaDoOnibusFragment fragment) {
		this.mapa = mapa;
		this.fragment = fragment;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mapa.limpa();
		if (tab.getTag().equals(BusaoNoMapa.ITINERARIO)) {
			fragment.exibePontosNoMapa();
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
