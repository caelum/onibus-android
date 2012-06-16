package br.com.caelum.ondeestaobusao.gps;

import java.util.Collection;
import java.util.LinkedList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.model.Coordenada;

public class GPSControl {
	private static final long TIME = 120000;
	private static final int DISTANCE = 5;
	private final BusaoActivity activity;
	private LocationListener locationListener;
	private LocationManager locationManager;
	
	private Collection<GPSObserver> observers = new LinkedList<GPSObserver>();
	private Coordenada atual;

	public GPSControl(BusaoActivity activity) {
		this.activity = activity;
		this.locationListener = createLocationListener();
	}

	private LocationListener createLocationListener() {
		return new LocationListener() {
			public void onLocationChanged(Location location) {
				makeUseLocation(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
	}

	public void execute() {
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.i("Provider", "GPS");
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME, DISTANCE, locationListener);
		}

		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Log.i("Provider", "NETWORK");
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, locationListener);
		}
		activity.atualizaTextoDoProgress(R.string.carregando_gps);
		Log.i("BUSCANDO LOCALIZACAO","");
	}
	
	public void shutdown() {
		locationManager.removeUpdates(locationListener);
		locationManager.removeUpdates(locationListener);
	}

	private void makeUseLocation(Location location) {
		if (location != null) {
			this.atual = new Coordenada(location.getLatitude(), location.getLongitude());
			Log.i("NOTIFICANDO OBSERVERS","");
			for (GPSObserver observer : observers) {
				observer.callback(atual);
			}
		} else {
			Toast.makeText(activity, 
					activity.getResources().getString(R.string.erro_localizacao_indeterminada),
					Toast.LENGTH_LONG).show();
			
			activity.onBackPressed();
		}
	}

	public void registerObserver(GPSObserver observer) {
		if (atual != null) {
			observer.callback(atual);
		}
		Log.i("OBSERVER ADICIONADO","");
		this.observers.add(observer);
	}

}
