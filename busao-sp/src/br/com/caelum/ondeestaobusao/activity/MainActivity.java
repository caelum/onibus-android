package br.com.caelum.ondeestaobusao.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.evento.Evento;
import br.com.caelum.ondeestaobusao.evento.PontosProximosEncontrados;
import br.com.caelum.ondeestaobusao.evento.delegate.PontosEncontradosContextDelegate;
import br.com.caelum.ondeestaobusao.gps.CentralNotificacoes;
import br.com.caelum.ondeestaobusao.gps.LidaComLocalizacao;
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
	private EstadoMainActivity estadoAtual;
	private ArrayList<Ponto> pontos;
	private boolean visualizacaoMapa;

	private Evento pontosEncontrados;
	private LidaComLocalizacao localizacao;

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
			this.visualizacaoMapa = savedInstanceState.getBoolean(
					"visualizacaoEmMapa", false);
		}

		application = (BusaoApplication) getApplication();

		if (application.getCentralNotificacoes() == null) {
			application.setCentralNotificacoes(new CentralNotificacoes(this));
		}

		localizacao = new LidaComLocalizacao(this);

		if (estadoAtual == null) {
			estadoAtual = EstadoMainActivity.inicio().executaEvolucao(this);
		}

		initializeSherlockActionBar();
		
		application.getCentralNotificacoes().registerObserver(this);
		pontosEncontrados = PontosProximosEncontrados.registraObservador(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		localizacao.start();
	}

	@Override
	protected void onStop() {
		localizacao.stop();
//		application.setUltimaLocalizacao(null);
		super.onStop();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("estado", this.estadoAtual);
		outState.putSerializable("pontos", this.pontos);
		outState.putBoolean("visualizacaoEmMapa", this.visualizacaoMapa);
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
		MyLog.i("Nova localizacao!" + localAtual);
		if (localAtual != null) {
			Coordenada ultimoLocal = application.getUltimaLocalizacao();
			
			if (pontos == null || localAtual.suficientementeDistanteDe(ultimoLocal)) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		localizacao.onActivityResult(requestCode, resultCode, data);
	}

}
