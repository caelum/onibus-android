package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.R.id;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.PontoComOnibusesOverlay;
import br.com.caelum.ondeestaobusao.map.PontoOverlayItem;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

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
		PontoComOnibusesOverlay overlay = new PontoComOnibusesOverlay(activity);
		
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
		TextView titulo = (TextView) view.findViewById(id.fragment_name);
		titulo.setText(getActivity().getResources().getString(R.string.procure_no_mapa));
	}

	@Override
	public void callback(Coordenada coordenada) {
		this.nossoMapa.centralizaNa(coordenada);
		
		dealWithResult(pontos);
	}
}
