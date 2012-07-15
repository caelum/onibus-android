package br.com.caelum.ondeestaobusao.gps;

import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationListenerImpl implements LocationListener, Listener {
	
	private LocationControl control;

	public LocationListenerImpl(LocationControl control) {
		this.control = control;
	}

	@Override
	public void onLocationChanged(Location location) {
		control.makeUseLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		
	}

}
