package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosDoOnibusTask extends GetJsonAsyncTask<Long, List<Ponto>> {
	private final String server_url = "itinerarioDoOnibus.json?onibus=%s";
	
	public PontosDoOnibusTask(Cache cache, AsyncResultDelegate<List<Ponto>> delegate) {
		super(cache, delegate);
	}

	@Override
	public String getFormatedURL(Long... params) {
		return String.format(MyServer.uriFor(server_url), params[0]);
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
