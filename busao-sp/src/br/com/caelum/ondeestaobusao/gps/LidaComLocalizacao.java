package br.com.caelum.ondeestaobusao.gps;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LidaComLocalizacao implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	// Update frequency in seconds
	public static final int UPDATE_INTERVAL_IN_SECONDS = 120;
	// Update frequency in milliseconds
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// The fastest update frequency, in seconds
	private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
	// A fast frequency ceiling in milliseconds
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND
			* FASTEST_INTERVAL_IN_SECONDS;
	private Activity activity;
	
	private LocationClient locationClient;
	private LocationRequest locationRequest;
	
	public LidaComLocalizacao(Activity activity) {
		this.activity = activity;
		
		if (servicesConnected()) {
			locationClient = new LocationClient(activity, this, this);

			locationRequest = LocationRequest.create();
			// Use high accuracy
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			// Set the update interval to 5 seconds
			locationRequest.setInterval(UPDATE_INTERVAL);
			// Set the fastest update interval to 1 second
			locationRequest.setFastestInterval(FASTEST_INTERVAL);
		}
	}
	
	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(activity);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			return false;
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				break;
			}
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(activity,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			// showErrorDialog(connectionResult.getErrorCode());
			Toast.makeText(activity, "Error: " + connectionResult.getErrorCode(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
//		Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
		locationClient.requestLocationUpdates(locationRequest, this);
	}
	
	public void stop() {
		if (locationClient.isConnected()) {
			locationClient.removeLocationUpdates(this);
		}
		locationClient.disconnect();
	}
	
	public void start() {
		locationClient.connect();
	}

	@Override
	public void onDisconnected() {
//		Toast.makeText(activity, "Disconnected. Please re-connect.",
//				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLocationChanged(Location location) {
//		String msg = "Updated Location: "
//				+ Double.toString(location.getLatitude()) + ","
//				+ Double.toString(location.getLongitude());
//		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		
		((BusaoApplication) activity.getApplication()).getCentralNotificacoes().makeUseLocation(location);
	}
	
	
	
}
