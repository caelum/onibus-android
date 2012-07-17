package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.gps.LocationObserver;
import br.com.caelum.ondeestaobusao.map.PontoDoOnibusOverlay;
import br.com.caelum.ondeestaobusao.map.VeiculosOverlay;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.model.Veiculo;
import br.com.caelum.ondeestaobusao.task.PontosDoOnibusTask;
import br.com.caelum.ondeestaobusao.task.VeiculoEmTempoRealTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.util.GeoCoderUtil;
import br.com.caelum.ondeestaobusao.util.Mapa;
import br.com.caelum.ondeestaobusao.widgets.actionbar.BusaoNoMapa;
import br.com.caelum.ondeestaobusao.widgets.actionbar.BusaoNoMapaListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;

public class MapaDoOnibusFragment extends SherlockFragment implements AsyncResultDelegate<List<Ponto>>, LocationObserver {
	private Onibus onibus;
	private AsyncTask<Long, Void, List<Ponto>> pontosDoOnibusTask;
	private List<Ponto> pontos;
	private Mapa mapa;
	private BusaoActivity activity;
	private PontoDoOnibusOverlay pontoOverlay;
	private List<Veiculo> veiculos;
	private VeiculosOverlay veiculosOverlay;
	private GeoCoderUtil geoCoderUtil;
	private AsyncTask<Integer, Void, List<Veiculo>> veiculosTask;
	private AlertDialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
		this.onibus = (Onibus) getArguments().getSerializable(Extras.ONIBUS);
		
		getSherlockActivity().getSupportActionBar().setTitle(onibus.getLetreiro());
		getSherlockActivity().getSupportActionBar().setSubtitle(onibus.getSentido().toString());
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		activity = (BusaoActivity) getActivity();
		activity.getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_TABS);
		
		BusaoApplication application = (BusaoApplication) getSherlockActivity().getApplication();
		mapa = application.getMapa();

		BusaoNoMapaListener listener = new BusaoNoMapaListener(this);
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
		
		application.getLocation().registerObserver(this);

		return mapa.getMapViewContainer();
	}


	@Override
	public void callback(Coordenada coordenada) {
		mapa.centralizaNa(coordenada);
		mapa.habilitaBussula();

		BusaoApplication application = (BusaoApplication) activity.getApplication();
		application.getProgressBar().showWithText(R.string.buscando_pontos_proximos);
		pontosDoOnibusTask = new PontosDoOnibusTask(this).execute(onibus.getId());
		veiculosTask = new VeiculoEmTempoRealTask(assync).execute(onibus.getCodigoGPS());
		Log.i("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%", onibus.getCodigoGPS()+ "");
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		this.pontos = pontos;
		exibePontosNoMapa();
	}

	public void exibePontosNoMapa() {
		pontoOverlay = new PontoDoOnibusOverlay(activity);
		
		BusaoApplication application = (BusaoApplication) activity.getApplication();
		mapa = application.getMapa();
		
		pontoOverlay.clear();
		if (pontos != null){
			for (Ponto ponto : pontos) {
				pontoOverlay.addOverlay(ponto.toOverlayItem());
			}
		}
		mapa.adicionaCamada(pontoOverlay);
		mapa.redesenha();
		
		application.getProgressBar().showWithText(R.string.colocando_pontos_mapa);
		application.getProgressBar().hide();
	}

	public void exibeVeiculosNoMapa() {
		geoCoderUtil = new GeoCoderUtil(activity);
		veiculosOverlay = new VeiculosOverlay(activity);
		
		BusaoApplication application = (BusaoApplication) activity.getApplication();
		mapa = application.getMapa();
		
		veiculosOverlay.clear();
		if (veiculos != null){
			for (Veiculo veiculo : veiculos) {
				veiculosOverlay.addOverlay(veiculo.toOverlayItem(geoCoderUtil.getEndereco(veiculo.getLocalizacao().toGeoPoint())));
			}
		}
		mapa.adicionaCamada(veiculosOverlay);
		mapa.redesenha();
		
		application.getProgressBar().showWithText(R.string.colocando_pontos_mapa);
		application.getProgressBar().hide();
	}
	
	public void error() {
		if (dialog == null) {
			cancelAssyncTasks();
			dialog = AlertDialogBuilder.builder(activity);
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, activity.getResources().getString(R.string.tente_novamente), new OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					MapaDoOnibusFragment.this.dialog = null;
					pontosDoOnibusTask = new PontosDoOnibusTask(MapaDoOnibusFragment.this).execute(onibus.getId());
					veiculosTask = new VeiculoEmTempoRealTask(assync).execute(onibus.getCodigoGPS());
				}
	
			});
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getResources().getString(R.string.cancelar), new OnClickListener() {
	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					activity.onBackPressed();
				}
	
			});
			dialog.show();
		}
	}

	@Override
	public void dealWithError() {
		this.error();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		mapa.desabilitaBussula();
		BusaoApplication application = (BusaoApplication) activity.getApplication();
		application.getProgressBar().hide();
		cancelAssyncTasks();

		getSherlockActivity().getSupportActionBar().setNavigationMode(
				ActionBar.NAVIGATION_MODE_STANDARD);
		
		mapa.limpa();

		mapa.removeDaTela();
	}


	private void cancelAssyncTasks() {
		if (pontosDoOnibusTask != null && Status.RUNNING.equals(pontosDoOnibusTask.getStatus())) {
			pontosDoOnibusTask.cancel(true);
		}
		
		if (veiculosTask != null && Status.RUNNING.equals(veiculosTask.getStatus())) {
			veiculosTask.cancel(true);
		}
	}
	
	private AsyncResultDelegate<List<Veiculo>> assync = new AsyncResultDelegate<List<Veiculo>>() {


		@Override
		public void dealWithResult(List<Veiculo> veiculos) {
			MapaDoOnibusFragment.this.veiculos = veiculos;
			exibeVeiculosNoMapa();
		}

		@Override
		public void dealWithError() {
			MapaDoOnibusFragment.this.error();
		}
	};
	
}
