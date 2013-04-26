package br.com.caelum.ondeestaobusao.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import br.com.caelum.ondeestaobusao.activity.MainActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PontosProximosMapaFragment extends SupportMapFragment {

	public static PontosProximosMapaFragment novoMapa(Coordenada coordenada) {
		PontosProximosMapaFragment fragment = new PontosProximosMapaFragment();

		Bundle bundle = new Bundle();
		fragment.setArguments(bundle);

		bundle.putSerializable("centro", coordenada);

		return fragment;
	}

	@Override
	public void onResume() {
		super.onResume();

		List<Ponto> pontos = ((MainActivity)getActivity()).getPontos();
		if (pontos!= null) {
			colocaPontosNoMapa((ArrayList<Ponto>) pontos);
		}

		if (getArguments() != null&& getArguments().getSerializable("centro") != null) {
			Coordenada coordenada = (Coordenada) getArguments()
					.getSerializable("centro");
			
			MarkerOptions marcador = new MarkerOptions()
			.position(coordenada.toLatLng())
			.title("Minha localizacao");

	getMap().addMarker(marcador);
			
			centralizaNo(coordenada.toLatLng());
		}
	}

	public void colocaPontosNoMapa(ArrayList<Ponto> pontos) {
		getArguments().putSerializable("pontos", pontos);
		
		for (Ponto ponto : pontos) {
			MarkerOptions marcador = new MarkerOptions()
					.position(ponto.getCoordenada().toLatLng())
					.title(ponto.getNome()).snippet(ponto.getDescricao())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop));

			getMap().addMarker(marcador);
		}
	}

	public void centralizaNo(LatLng local) {
		GoogleMap mapa = getMap();

		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 17));
	}
}
