package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

@SuppressWarnings("serial")
public class Coordenada implements Serializable{
	private static final double CONVERSION_SCALE = 1E6;
	private final Double latitude;
	private final Double longitude;

	public Coordenada(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Coordenada(GeoPoint geoPoint) {
		this.latitude = geoPoint.getLatitudeE6()  / CONVERSION_SCALE;
		this.longitude = geoPoint.getLongitudeE6() / CONVERSION_SCALE;
	}
	
	public GeoPoint toGeoPoint() {
		Double geoLat = this.latitude*CONVERSION_SCALE;
		Double geoLng = this.longitude*CONVERSION_SCALE;
		return new GeoPoint(geoLat.intValue(), geoLng.intValue());
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
