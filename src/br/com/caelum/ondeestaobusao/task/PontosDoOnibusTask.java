package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosDoOnibusTask extends GetJsonAsyncTask<Long, List<Ponto>> {
	private final String server_url = "http://ondeestaoalbi2.herokuapp.com/itinerarioDoOnibus.json?onibus=%s";
	
	public PontosDoOnibusTask(AsyncResultDelegate<List<Ponto>> delegate) {
		super(delegate);
	}

	@Override
	public String getFormatedURL(Long... params) {
		return String.format(server_url, params[0]);
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
