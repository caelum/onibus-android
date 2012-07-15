package br.com.caelum.ondeestaobusao.gps;

import java.util.Collection;
import java.util.LinkedList;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.model.Coordenada;

public class LocationControl {
	private static final long TIME = 120000;
	private static final int DISTANCE = 5;
	private LocationListenerImpl locationListener;
	private LocationManager locationManager;
	
	private Collection<LocationObserver> observers = new LinkedList<LocationObserver>();
	private Coordenada atual;

	public LocationControl(BusaoActivity activity) {
		this.locationListener = new LocationListenerImpl(this);
		this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
	}

	public void execute() {

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, locationListener);
		}

		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, locationListener);
		}
	}
	
	public void shutdown() {
		locationManager.removeUpdates(locationListener);
		locationManager.removeGpsStatusListener(locationListener);
	}
	
	public void makeUseLocation(Location location) {
		if (location != null) {
			this.atual = new Coordenada(location.getLatitude(), location.getLongitude());
			for (LocationObserver observer : observers) {
				observer.callback(atual);
			}
		} else {
			//TODO TRATAR O ERRO AQUI
		}
	}

	public void registerObserver(LocationObserver observer) {
		if (atual != null) {
			observer.callback(atual);
		}
		this.observers.add(observer);
	}
	
	public void unRegisterObserver(LocationObserver observer) {
		this.observers.remove(observer);
	}
	
	public Coordenada getAtual() {
		return atual;
	}

}
