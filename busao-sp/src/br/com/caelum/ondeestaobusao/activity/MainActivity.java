package br.com.caelum.ondeestaobusao.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.activity.service.LocationService;
import br.com.caelum.ondeestaobusao.evento.Evento;
import br.com.caelum.ondeestaobusao.evento.PontosProximosEncontrados;
import br.com.caelum.ondeestaobusao.evento.delegate.PontosEncontradosContextDelegate;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosMapaFragment;
import br.com.caelum.ondeestaobusao.gps.CentralNotificacoes;
import br.com.caelum.ondeestaobusao.gps.LocationObserver;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.util.MyLog;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements
		LocationObserver, PontosEncontradosContextDelegate {
	private BusaoApplication application;
	private Intent locationIntent;
	private EstadoMainActivity estadoAtual;
	private ArrayList<Ponto> pontos;
	private boolean visualizacaoMapa;

	private Evento pontosEncontrados;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		AppRater.app_launched(this);

		if (savedInstanceState != null) {
			this.pontos = (ArrayList<Ponto>) savedInstanceState
					.getSerializable("pontos");
			this.estadoAtual = (EstadoMainActivity) savedInstanceState
					.get("estado");
			this.visualizacaoMapa = savedInstanceState.getBoolean("visualizacaoEmMapa", false);
		}

		pontosEncontrados = PontosProximosEncontrados.registraObservador(this);

		application = (BusaoApplication) getApplication();

		application.getCentralNotificacoes().registerObserver(this);

		if (estadoAtual == null) {
			estadoAtual = EstadoMainActivity.inicio().executaEvolucao(this);
		}

		feioPracarai();

		initializeSherlockActionBar();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("estado", this.estadoAtual);
		outState.putSerializable("pontos", this.pontos);
		outState.putBoolean("visualizacaoEmMapa", this.visualizacaoMapa);
	}

	private void feioPracarai() {
		if (application.getCentralNotificacoes() == null) {
			application.setCentralNotificacoes(new CentralNotificacoes(this));
		}
		restartLocationService();
	}

	private void restartLocationService() {
		stopLocationService();
		locationIntent = new Intent(this, LocationService.class);
		startService(locationIntent);
	}

	private void stopLocationService() {
		if (locationIntent != null) {
			stopService(locationIntent);
		}
	}

	private void initializeSherlockActionBar() {
		this.getSupportActionBar()
				.setTitle(getString(R.string.pontos_proximos));
		this.getSupportActionBar().setSubtitle(null);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_atualizar:
			estadoAtual = EstadoMainActivity.inicio().executaEvolucao(this);

			restartLocationService();
			break;
			
		case R.id.menu_mapa_onibus_proximos: 
			this.visualizacaoMapa = !this.visualizacaoMapa;
			estadoAtual = estadoAtual.executaEvolucao(this);
			
			break;
			
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	public boolean isVisualizacaoMapa() {
		return visualizacaoMapa;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		stopLocationService();
		locationIntent = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_principal, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.getCentralNotificacoes().unRegisterObserver(this);
		pontosEncontrados.unregister(application);
	}

	@Override
	public void mudouLocalizacaoPara(Coordenada localAtual) {
		stopLocationService();
		
		MyLog.i("Nova localizacao!" + localAtual);
		if (localAtual != null) {
			Coordenada ultimoLocal = application.getUltimaLocalizacao();

			if (localAtual.suficientementeDistanteDe(ultimoLocal)) {
				MyLog.i("Buscando pontos!" + localAtual);
				MyLog.i("Discancia: "
						+ (ultimoLocal == null ? "?" : Float
								.toString(localAtual
										.distanciaEmMetrosDa(ultimoLocal))));

				application.setUltimaLocalizacao(localAtual);
				puscaPontosParaLocalizacao();
			}
		}
	}

	private void puscaPontosParaLocalizacao() {
		estadoAtual = estadoAtual.executaEvolucao(this);
		PontosEOnibusTask task = new PontosEOnibusTask(application);
		task.execute(application.getUltimaLocalizacao());
	}

	@Override
	public void lidaComPontosProximos(ArrayList<Ponto> pontos) {
		this.pontos = pontos;
		estadoAtual = estadoAtual.executaEvolucao(this);
	}

	@Override
	public void lidaComFalha(String mensagem) {
		AlertDialog.Builder dialog = AlertDialogBuilder.builder(this);
		dialog.setPositiveButton(mensagem, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				puscaPontosParaLocalizacao();
			}
		});
		dialog.setNegativeButton(
				this.getResources().getString(R.string.cancelar),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// TODO ...
					}
				});
		dialog.show();
	}

	@Override
	public Context getContext() {
		return this;
	}

	public ArrayList<Ponto> getPontos() {
		return pontos;
	}
}
