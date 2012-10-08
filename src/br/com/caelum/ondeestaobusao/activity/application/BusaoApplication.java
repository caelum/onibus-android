package br.com.caelum.ondeestaobusao.activity.application;

import android.app.Application;
import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.gps.LocationControl;

public class BusaoApplication extends Application {
	private LocationControl location;
	private Cache cache;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		cache = new Cache();
	}
	
	public void setLocation(LocationControl control) {
		this.location = control;
	}

	public LocationControl getLocation() {
		return location;
	}

	public Cache getCache() {
		return cache;
	}

}
