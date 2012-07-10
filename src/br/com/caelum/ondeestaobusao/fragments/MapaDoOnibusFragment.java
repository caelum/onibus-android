package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.annotation.SuppressLint;
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
import br.com.caelum.ondeestaobusao.widgets.actionbar.BusaoNoMapa;
import br.com.caelum.ondeestaobusao.widgets.actionbar.BusaoNoMapaListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

@SuppressLint("ValidFragment")
public class MapaDoOnibusFragment extends GPSFragment implements AsyncResultDelegate<List<Ponto>> {
	private Mapa mapa;
	private BusaoActivity activity;
	private Onibus onibus;
	private AsyncTask<Long, Void, List<Ponto>> pontosDoOnibusTask;
	private List<Ponto> pontos;
	
	public MapaDoOnibusFragment(BusaoActivity activity) {
		super(activity.getGps());
		this.activity = activity;
		this.mapa = new Mapa(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		this.onibus = (Onibus) getArguments().getSerializable(Extras.ONIBUS);
		
		activity.getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
		
		BusaoNoMapaListener listener = new BusaoNoMapaListener(mapa, this);
		ActionBar actionBar = activity.getSupportActionBar();
		
		actionBar.removeAllTabs();
		
		Tab itinerario = actionBar.newTab();
		itinerario.setText("Itinerarios");
		itinerario.setTag(BusaoNoMapa.ITINERARIO);
		itinerario.setTabListener(listener);
		actionBar.addTab(itinerario, true);
		
		Tab tempoReal = actionBar.newTab();
		tempoReal.setText("Tempo real");
		tempoReal.setTag(BusaoNoMapa.TEMPO_REAL);
		tempoReal.setTabListener(listener);
		actionBar.addTab(tempoReal);
		
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
		this.pontos = pontos;
		exibePontosNoMapa();
		
		activity.escondeProgress();
	}

	public void exibePontosNoMapa() {
		PontoDoOnibusOverlay pontoOverlay = new PontoDoOnibusOverlay(activity);
		
		pontoOverlay.clear();
		if (pontos != null){
			for (Ponto ponto : pontos) {
				pontoOverlay.addOverlay(ponto.toOverlayItem());
			}
		}
		mapa.adicionaCamada(pontoOverlay);
		mapa.redesenha();
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

		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		
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
