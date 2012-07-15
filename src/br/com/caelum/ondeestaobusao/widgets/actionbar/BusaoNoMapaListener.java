package br.com.caelum.ondeestaobusao.widgets.actionbar;

import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.fragments.MapaDoOnibusFragment;
import br.com.caelum.ondeestaobusao.util.Mapa;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

public class BusaoNoMapaListener implements TabListener {
	private final Mapa mapa;
	private final MapaDoOnibusFragment fragment;

	public BusaoNoMapaListener(MapaDoOnibusFragment fragment) {
		BusaoApplication application = (BusaoApplication) fragment.getSherlockActivity().getApplication();
		this.fragment = fragment;
		this.mapa = application.getMapa();
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mapa.limpa();
		if (tab.getTag().equals(BusaoNoMapa.ITINERARIO)) {
			fragment.exibePontosNoMapa();
		} else {
			fragment.exibeVeiculosNoMapa();
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
