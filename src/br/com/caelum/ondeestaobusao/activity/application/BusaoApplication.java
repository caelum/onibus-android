package br.com.caelum.ondeestaobusao.activity.application;

import android.app.Application;
import br.com.caelum.ondeestaobusao.gps.LocationControl;
import br.com.caelum.ondeestaobusao.progressbar.ProgressBarAdministrator;
import br.com.caelum.ondeestaobusao.util.Mapa;

public class BusaoApplication extends Application {
	private ProgressBarAdministrator progressBar;
	private LocationControl location;
	private Mapa mapa;
	
	public ProgressBarAdministrator getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBarAdministrator progressBar) {
		this.progressBar = progressBar;
	}

	public void setLocation(LocationControl control) {
		this.location = control;
	}

	public LocationControl getLocation() {
		return location;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	public Mapa getMapa() {
		return mapa;
	}
	
}
