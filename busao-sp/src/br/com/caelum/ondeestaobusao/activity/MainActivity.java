package br.com.caelum.ondeestaobusao.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.activity.service.LocationService;
import br.com.caelum.ondeestaobusao.eventos.Evento;
import br.com.caelum.ondeestaobusao.eventos.EventoPontosEncontrados;
import br.com.caelum.ondeestaobusao.eventos.PontosEncontradosContextDelegate;
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

public class MainActivity extends SherlockFragmentActivity implements LocationObserver, PontosEncontradosContextDelegate  {
	private BusaoApplication application;
	private Intent locationIntent;
	private EstadoMainActivity estadoAtual;
	private ArrayList<Ponto> pontos;
	
	private Evento pontosEncontrados;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		AppRater.app_launched(this);
		
		if (savedInstanceState != null) {
			this.pontos = (ArrayList<Ponto>) savedInstanceState.getSerializable("pontos");
			this.estadoAtual = (EstadoMainActivity) savedInstanceState.get("estado");
		}
		
		pontosEncontrados = EventoPontosEncontrados.registraObservador(this);
		
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
	}

	private void feioPracarai() {
		if (application.getCentralNotificacoes() == null) {
			application.setCentralNotificacoes(new CentralNotificacoes(this));
		}
		if (locationIntent != null) {
			stopService(locationIntent);
		}
		locationIntent = new Intent(this, LocationService.class);
		startService(locationIntent);
	}

	private void initializeSherlockActionBar() {
		this.getSupportActionBar().setTitle(getString(R.string.pontos_proximos));
		this.getSupportActionBar().setSubtitle(null);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_atualizar) {

			estadoAtual = EstadoMainActivity.inicio().executaEvolucao(this);
			
			if (locationIntent != null) {
				stopService(locationIntent);
				startService(locationIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	
	@Override
	protected void onStop() {
		super.onStop();
		if (locationIntent != null) {
			stopService(locationIntent);
			locationIntent = null;
		}
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
		MyLog.i("Nova localizacao!"+localAtual);
		if (localAtual != null) {
			Coordenada ultimoLocal = application.getUltimaLocalizacao();
			
			if (localAtual.suficientementeDistanteDe(ultimoLocal)) {
				MyLog.i("Buscando pontos!"+localAtual);
				MyLog.i("Discancia: "+(ultimoLocal == null ? "?" : Float.toString(localAtual.distanciaEmMetrosDa(ultimoLocal))));
				
				application.setUltimaLocalizacao(localAtual);
				puscaPontosParaLocalizacao();
			}
		}
	}

	private void puscaPontosParaLocalizacao() {
		estadoAtual = estadoAtual.executaEvolucao(this);
		PontosEOnibusTask task = new PontosEOnibusTask(application.getCache(), MainActivity.this);
		task.execute(application.getUltimaLocalizacao());
		
		application.add(task);
	}

	@Override
	public void lidaCom(ArrayList<Ponto> pontos) {
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
		dialog.setNegativeButton(this.getResources().getString(R.string.cancelar), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//TODO ...
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
