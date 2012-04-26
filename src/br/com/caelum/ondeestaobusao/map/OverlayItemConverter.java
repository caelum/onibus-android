package br.com.caelum.ondeestaobusao.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import br.com.caelum.ondeestaobusao.model.Coordenada;

import com.google.android.maps.OverlayItem;

public class OverlayItemConverter {
	private final Geocoder geoCoder;

	public OverlayItemConverter(Context context) {
		geoCoder = new Geocoder(context, new Locale("pt","BR"));
	}

	public OverlayItem convert(Coordenada coordenada) {
		try {
			List<Address> addresses = geoCoder.getFromLocation(coordenada.getLatitude(), coordenada.getLongitude(), 1);

			if (!addresses.isEmpty()) {
				String address = "";
				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
					address += addresses.get(0).getAddressLine(i) + "\n";
				}	
				return new OverlayItem(coordenada.toGeoPoint(), "Endereço:", address);
			}
		} catch (IOException e) {
			Log.e("GeoCoder", "Erro no GeoCoder", e);
		}
		return new OverlayItem(coordenada.toGeoPoint(), "Endereço", "Endereço desconhecido");
	}
	
}
