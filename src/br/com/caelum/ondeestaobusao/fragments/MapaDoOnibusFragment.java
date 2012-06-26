package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.map.PontoDoOnibusOverlay;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;

public class MapaDoOnibusFragment extends GPSFragment implements AsyncResultDelegate<List<Ponto>> {
	private Mapa mapa;
	private BusaoActivity activity;
	private Onibus onibus;
	private AsyncTask<Long, Void, List<Ponto>> pontosDoOnibusTask;
	
	public MapaDoOnibusFragment(BusaoActivity activity) {
		super(activity.getGps());
		this.activity = activity;
		this.mapa = new Mapa(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		this.onibus = (Onibus) getArguments().getSerializable(Extras.ONIBUS);
		return mapa.getMapViewContainer();
	}


	@Override
	public void callback(Coordenada coordenada) {
		mapa.centralizaNa(coordenada);
		mapa.habilitaBussula();

		activity.exibeProgress();
		activity.atualizaTextoDoProgress(R.string.buscando_pontos_onibus);
		pontosDoOnibusTask = new PontosDoOnibusTask(this).execute(onibus.getId());
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		PontoDoOnibusOverlay pontoOverlay = new PontoDoOnibusOverlay(activity);
		
		pontoOverlay.clear();
		
		for (Ponto ponto : pontos) {
			pontoOverlay.addOverlay(ponto.toOverlayItem());
		}
		
		mapa.adicionaCamada(pontoOverlay);
		mapa.redesenha();
		
		activity.escondeProgress();
	}

	@Override
	public void dealWithError() {
		activity.dealWithError();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (pontosDoOnibusTask != null && Status.RUNNING.equals(pontosDoOnibusTask.getStatus())) {
			pontosDoOnibusTask.cancel(true);
		}

		mapa.limpa();

		mapa.removeDaTela();
	}

	@Override
	public void updateHeader(View view) {
		getSherlockActivity().getSupportActionBar().setTitle(onibus.getLetreiro());
		getSherlockActivity().getSupportActionBar().setSubtitle(onibus.getSentido().toString());
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
