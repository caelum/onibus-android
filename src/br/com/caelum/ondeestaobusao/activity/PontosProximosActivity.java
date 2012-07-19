package br.com.caelum.ondeestaobusao.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.activity.service.LocationService;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.gps.LocationControl;
import br.com.caelum.ondeestaobusao.gps.LocationObserver;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.progressbar.ProgressBarAdministrator;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.util.AlertDialogBuilder;
import br.com.caelum.ondeestaobusao.util.CancelableAssynTask;
import br.com.caelum.ondeestaobusao.widget.AppRater;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PontosProximosActivity extends SherlockActivity implements AsyncResultDelegate<List<Ponto>>,
		LocationObserver {

	private BusaoApplication application;
	private Intent locationIntent;
	private PontoExpandableListView pontosExpandableListView;
	private Coordenada coordenada;
	private AsyncTask<Coordenada, Void, List<Ponto>> pontosEOnibusTask;
	private ProgressBarAdministrator progressBarAdministrator;
	private Cache cache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AppRater.app_launched(this);

		application = (BusaoApplication) getApplication();

		cache = application.getCache();
		if (application.getLocation() == null) {
			application.setLocation(new LocationControl(this));
		}
		if (locationIntent == null) {
			locationIntent = new Intent(this, LocationService.class);
			startService(locationIntent);
		}

		progressBarAdministrator = new ProgressBarAdministrator(this);
		pontosExpandableListView = (PontoExpandableListView) findViewById(R.id.listPonto);

		application.getLocation().registerObserver(this);
		initializeSherlockActionBar();
	}

	private void initializeSherlockActionBar() {
		this.getSupportActionBar().setTitle(getString(R.string.pontos_proximos));
		this.getSupportActionBar().setSubtitle(null);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_atualizar) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void callback(Coordenada coordenada) {
		this.coordenada = coordenada;

		hide();
		progressBarAdministrator.showWithText(R.string.buscando_pontos_proximos);
		pontosEOnibusTask = new PontosEOnibusTask(cache, this).execute(coordenada);
	}

	private void hide() {
		pontosExpandableListView.setVisibility(View.GONE);
	}

	@Override
	public void dealWithResult(final List<Ponto> pontos) {
		pontosExpandableListView.setAdapter(new PontosEOnibusAdapter(pontos, this));

		pontosExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);

				Intent intent = new Intent(PontosProximosActivity.this, MapaDoOnibusActivity.class);

				intent.putExtra(Extras.ONIBUS, onibus);

				startActivity(intent);

				return false;
			}
		});
		pontosExpandableListView.setVisibility(View.VISIBLE);
		progressBarAdministrator.hide();
	}

	@Override
	public void dealWithError() {
		AlertDialog.Builder dialog = AlertDialogBuilder.builder(this);
		dialog.setPositiveButton(this.getResources().getString(R.string.tente_novamente), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				pontosEOnibusTask = new PontosEOnibusTask(cache, PontosProximosActivity.this).execute(coordenada);
			}

		});
		dialog.setNegativeButton(this.getResources().getString(R.string.cancelar), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				PontosProximosActivity.this.onBackPressed();
			}

		});
		dialog.show();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		CancelableAssynTask.cancel(pontosEOnibusTask);

		application.getLocation().unRegisterObserver(this);
		
		if (locationIntent != null) {
			stopService(locationIntent);
			locationIntent = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_split_principal, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
