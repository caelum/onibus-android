package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.net.URLEncoder;

import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.delegate.GetJsonDelegate;
import br.com.caelum.ondeestaobusao.model.Destino;

import com.google.gson.reflect.TypeToken;

public class DestinosTask implements GetJsonDelegate<String, Destino> {
	private final String server_url = "http://ondeestaoalbi.herokuapp.com/destinoParaEndereco/%s.json";
	private final AsyncResultDelegate<Destino> delegate;
	
	public DestinosTask(AsyncResultDelegate<Destino> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void doOnPostExecute(Destino result) {
		delegate.dealWithResult(result);
	}

	@Override
	public String getFormatedURL(String... params) {
		String uri = URLEncoder.encode(params[0]);
		return String.format(server_url, uri);
	}

	@Override
	public Type getElementType() {
		return new TypeToken<Destino>() {}.getType();
	}

	@Override
	public void doOnError(Exception e) {
		delegate.dealWithError();
	}

	@Override
	public Destino onErrorReturn() {
		return null;
	}

}