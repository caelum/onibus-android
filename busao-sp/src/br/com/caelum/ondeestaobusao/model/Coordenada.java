package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

@SuppressWarnings("serial")
public class Coordenada implements Serializable{
	private final Double latitude;
	private final Double longitude;

	public Coordenada(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Coordenada(LatLng geoPoint) {
		this.latitude = geoPoint.latitude;
		this.longitude = geoPoint.longitude;
	}
	
	public LatLng toGeoPoint() {
		return new LatLng(this.latitude,this.longitude);
	}

	@Override
	public String toString() {
		return "("+latitude+", "+longitude+")";
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	
}
