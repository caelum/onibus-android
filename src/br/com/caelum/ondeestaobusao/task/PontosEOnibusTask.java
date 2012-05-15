package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.delegate.GetJsonDelegate;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosEOnibusTask implements GetJsonDelegate<Coordenada, List<Ponto>>{
	private final String server_url = "http://ondeestaoalbi.herokuapp.com/onibusesNosPontosProximos.json?lat=%s&long=%s";
	private final AsyncResultDelegate<List<Ponto>> delegate;

	public PontosEOnibusTask(AsyncResultDelegate<List<Ponto>> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void doOnPostExecute(List<Ponto> result) {
		delegate.dealWithResult(result);
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

	@Override
	public List<Ponto> onErrorReturn() {
		return new ArrayList<Ponto>();
	}

	@Override
	public void doOnError(Exception e) {
		delegate.dealWithError();
	}

}
