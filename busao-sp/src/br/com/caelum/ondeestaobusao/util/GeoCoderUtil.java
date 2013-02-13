package br.com.caelum.ondeestaobusao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class GeoCoderUtil {
	
	private Geocoder geo;

	public GeoCoderUtil(Context ctx) {
		geo = new Geocoder(ctx, Locale.getDefault());
	}
	

	public String getEndereco(LatLng geoPoint) {
		List<Address> enderecos = new ArrayList<Address>();
		try {
			enderecos = geo.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1);
			if (!enderecos.isEmpty()) {
				return enderecos.get(0).getAddressLine(0);
			}
		} catch (IOException e) {
		}
		
		return "";
	}
}
