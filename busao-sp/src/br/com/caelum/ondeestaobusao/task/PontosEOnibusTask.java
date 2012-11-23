package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosEOnibusTask extends GetJsonAsyncTask<Coordenada, List<Ponto>>{
	private final String server_url = "onibusesNosPontosProximos.json?lat=%s&long=%s";

	public PontosEOnibusTask(Cache cache, AsyncResultDelegate<List<Ponto>> delegate) {
		super(cache, delegate);
	}

	@Override
	public String getFormatedURL(Coordenada... params) {
		Coordenada coordenada = params[0];
		return String.format(MyServer.uriFor(server_url), coordenada.getLatitude(), coordenada.getLongitude());
	}

	@Override
	public Type getElementType() {
		return new TypeToken<List<Ponto>>() {}.getType();
	}

	@Override
	public List<Ponto> onErrorReturn() {
		return new ArrayList<Ponto>();
	}
}
