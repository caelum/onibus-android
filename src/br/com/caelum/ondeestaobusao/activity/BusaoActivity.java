package br.com.caelum.ondeestaobusao.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.activity.service.LocationService;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosFragment;
import br.com.caelum.ondeestaobusao.gps.LocationControl;
import br.com.caelum.ondeestaobusao.progressbar.ProgressBarAdministrator;
import br.com.caelum.ondeestaobusao.util.Mapa;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class BusaoActivity extends SherlockFragmentActivity {

	private BusaoApplication application;
	private PontosProximosFragment pontosProximosFragment;
	private Intent locationControl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		AppRater.app_launched(this);
		
		ProgressBarAdministrator progressBarAdministrator = new ProgressBarAdministrator(this);
		
		application = (BusaoApplication) getApplication();
		
		application.setMapa(new Mapa(this));
		application.setProgressBar(progressBarAdministrator);
		application.setLocation(new LocationControl());

		locationControl = new Intent(this, LocationService.class);
		startService(locationControl);
		
		pontosProximosFragment = new PontosProximosFragment();
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_main, pontosProximosFragment, pontosProximosFragment.getClass().getName()).commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if ((getSupportActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0) {
				onBackPressed();
			}
			return true;
		} else if (item.getItemId() == R.id.menu_atualizar) {
			pontosProximosFragment.hide();
			application.getProgressBar().showWithText(R.string.carregando_gps);
			stopService(locationControl);
			startService(locationControl);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void finish() {
		stopService(locationControl);
		super.finish();
	}
}
