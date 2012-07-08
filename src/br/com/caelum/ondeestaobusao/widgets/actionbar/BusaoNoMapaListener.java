package br.com.caelum.ondeestaobusao.widgets.actionbar;

import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.fragments.Mapa;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class BusaoNoMapaListener implements TabListener {
	private final Mapa mapa;

	public BusaoNoMapaListener(Mapa mapa) {
		this.mapa = mapa;
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getTag().equals(BusaoNoMapa.ITINERARIO)) {
			
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


}
