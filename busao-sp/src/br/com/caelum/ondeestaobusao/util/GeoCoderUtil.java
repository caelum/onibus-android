package br.com.caelum.ondeestaobusao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.maps.GeoPoint;

public class GeoCoderUtil {
	
	private static final double CONVERSION_SCALE = 1E6;
	
	private Geocoder geo;

	public GeoCoderUtil(Context ctx) {
		geo = new Geocoder(ctx, Locale.getDefault());
	}
	

	public String getEndereco(GeoPoint geoPoint) {
		Double latitude = geoPoint.getLatitudeE6()  / CONVERSION_SCALE;
		Double longitude = geoPoint.getLongitudeE6() / CONVERSION_SCALE;
		List<Address> enderecos = new ArrayList<Address>();
		try {
			enderecos = geo.getFromLocation(latitude, longitude, 1);
			if (!enderecos.isEmpty()) {
				return enderecos.get(0).getAddressLine(0);
			}
		} catch (IOException e) {
		}
		
		return "";
	}
}
