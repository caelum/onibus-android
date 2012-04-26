package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.List;

import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosEOnibusResolver implements GetJsonResolver<Coordenada, List<Ponto>>{
	private final String server_url = "http://ondeestaoalbi.herokuapp.com/onibusesNosPontosProximos.json?lat=%s&long=%s";
	private final AsyncResultDealer<List<Ponto>> activity;

	public PontosEOnibusResolver(AsyncResultDealer<List<Ponto>> activity) {
		this.activity = activity;
	}
	
	@Override
	public void doOnPostExecute(List<Ponto> result) {
		activity.dealWithResult(result);
	}

	@Override
	public String getFormatedURL(Coordenada... params) {
		Coordenada coordenada = params[0];
		return String.format(server_url, coordenada.getLatitude(), coordenada.getLongitude());
	}

	@Override
	public Type getElementType() {
		return new TypeToken<List<Ponto>>() {}.getType();
	}
}
