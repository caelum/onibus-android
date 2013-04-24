package br.com.caelum.ondeestaobusao.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.activity.service.LocationService;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosFragment;
import br.com.caelum.ondeestaobusao.fragments.ProgressFragment;
import br.com.caelum.ondeestaobusao.gps.CentralNotificacoes;
import br.com.caelum.ondeestaobusao.gps.LocationObserver;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.util.CancelableAssynTask;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements AsyncResultDelegate<ArrayList<Ponto>>, LocationObserver  {
	private BusaoApplication application;
	private Intent locationIntent;
	
	private PontosEOnibusTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.principal);
		AppRater.app_launched(this);
		
		application = (BusaoApplication) getApplication();

		application.getCentralNotificacoes().registerObserver(this);
		
		FragmentManager manager = getSupportFragmentManager();
		
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.principal_unico, ProgressFragment.comMensagem(R.string.carregando_gps));
		transaction.commit();
		
		feioPracarai();

		initializeSherlockActionBar();
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
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.principal_unico, ProgressFragment.comMensagem(R.string.carregando_gps));
			transaction.commit();
			
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
	public void dealWithResult(ArrayList<Ponto> pontos) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.principal_unico, PontosProximosFragment.comPontos(pontos), PontosProximosFragment.class.getName());
			transaction.commit();
	}

	@Override
	public void dealWithError() {
		AlertDialog.Builder dialog = AlertDialogBuilder.builder(this);
		dialog.setPositiveButton(this.getResources().getString(R.string.tente_novamente), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				task = new PontosEOnibusTask(application.getCache(), MainActivity.this);
				task.execute(application.getUltimaLocalizacao());
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
	protected void onDestroy() {
		super.onDestroy();
		application.getCentralNotificacoes().unRegisterObserver(this);
		CancelableAssynTask.cancel(task);
	}
	
	@Override
	public void mudouLocalizacaoPara(Coordenada coordenada) {
		application.setUltimaLocalizacao(coordenada);
		
		FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.principal_unico, ProgressFragment.comMensagem(R.string.buscando_pontos_proximos));
		transaction.commit();
		
		task = new PontosEOnibusTask(application.getCache(), this);
		task.execute(coordenada);
	}
}
