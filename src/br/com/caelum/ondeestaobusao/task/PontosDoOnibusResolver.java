package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.List;

import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosDoOnibusResolver implements GetJsonResolver<Long, List<Ponto>> {
	private final String server_url = "http://ondeestaoalbi.herokuapp.com/pontosDoOnibusSelecionado.json?onibus=%s";
	private final AsyncResultDealer<List<Ponto>> activity;
	
	public PontosDoOnibusResolver(AsyncResultDealer<List<Ponto>> activity) {
		this.activity = activity;
	}
	
	@Override
	public void doOnPostExecute(List<Ponto> result) {
		activity.dealWithResult(result);
	}

	@Override
	public String getFormatedURL(Long... params) {
		return String.format(server_url, params[0]);
	}

	@Override
	public Type getElementType() {
		return new TypeToken<List<Ponto>>() {}.getType();
	}
}
