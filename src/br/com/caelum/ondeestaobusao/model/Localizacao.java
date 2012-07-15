package br.com.caelum.ondeestaobusao.model;

import com.google.android.maps.GeoPoint;

public class Localizacao {
	private static final double CONVERSION_SCALE = 1E6;
	
	private Double latitude;
	private Double longitude;
	
	public Localizacao(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public GeoPoint toGeoPoint() {
		Double geoLat = this.longitude*CONVERSION_SCALE;
		Double geoLng = this.latitude*CONVERSION_SCALE;
		return new GeoPoint(geoLat.intValue(), geoLng.intValue());
	}
}
