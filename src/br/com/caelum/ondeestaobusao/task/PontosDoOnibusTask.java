package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.delegate.GetJsonDelegate;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosDoOnibusTask implements GetJsonDelegate<Long, List<Ponto>> {
	private final String server_url = "http://ondeestaoalbi.herokuapp.com/pontosDoOnibusSelecionado.json?onibus=%s";
	private final AsyncResultDelegate<List<Ponto>> delegate;
	
	public PontosDoOnibusTask(AsyncResultDelegate<List<Ponto>> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void doOnPostExecute(List<Ponto> result) {
		delegate.dealWithResult(result);
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

	@Override
	public void doOnError(Exception e) {
		delegate.dealWithError();
	}

}
