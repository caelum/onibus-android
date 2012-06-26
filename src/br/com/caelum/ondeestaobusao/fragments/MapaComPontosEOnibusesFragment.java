package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.PontoComOnibusesOverlay;
import br.com.caelum.ondeestaobusao.map.PontoOverlayItem;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;

public class MapaComPontosEOnibusesFragment extends GPSFragment implements AsyncResultDelegate<List<Ponto>> {
	private Mapa nossoMapa;
	private BusaoActivity activity;
	private List<Ponto> pontos;

	public MapaComPontosEOnibusesFragment(BusaoActivity activity, List<Ponto> pontos) {
		super(activity.getGps());
		this.activity = activity;
		this.nossoMapa = new Mapa(activity);
		this.pontos = pontos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		return nossoMapa.getMapViewContainer();
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		PontoComOnibusesOverlay overlay = new PontoComOnibusesOverlay(activity, this);
		
		for (Ponto ponto : pontos) {
			overlay.addOverlay(new PontoOverlayItem(ponto));
		}
		nossoMapa.adicionaCamada(overlay);
		nossoMapa.redesenha();
		
		activity.escondeProgress();
	}

	@Override
	public void dealWithError() {
		activity.dealWithError();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		nossoMapa.limpa();
		nossoMapa.removeDaTela();
	}

	@Override
	public void updateHeader(View view) {
		getSherlockActivity().getSupportActionBar().setTitle(getString(R.string.procure_no_mapa));
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void callback(Coordenada coordenada) {
		this.nossoMapa.centralizaNa(coordenada);
		
		if (pontos != null) {
			dealWithResult(pontos);
		} else {
			new PontosEOnibusTask(this).execute(coordenada);
			activity.atualizaTextoDoProgress(R.string.buscando_pontos_proximos);
		}
	}

	//TODO REVISAR ERICH	
	public void finaliza(Ponto ponto) {
		PontosProximosFragment pontosProximosFragment = (PontosProximosFragment) this.getFragmentManager().findFragmentByTag(PontosProximosFragment.class.getName());
		pontosProximosFragment.selecionaPonto(ponto);
		
		this.getFragmentManager().beginTransaction().remove(this).commit();
		this.getFragmentManager().popBackStackImmediate();
	}
}
