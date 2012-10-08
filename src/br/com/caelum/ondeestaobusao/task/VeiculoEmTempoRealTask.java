package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.model.Veiculo;

import com.google.gson.reflect.TypeToken;

public class VeiculoEmTempoRealTask extends GetJsonAsyncTask<Integer, List<Veiculo>> {
	private final String server_url = "http://ondeestaoalbi2.herokuapp.com/localizacoesDoOnibus.json?codigoLinha=%s";
	
	public VeiculoEmTempoRealTask(AsyncResultDelegate<List<Veiculo>> delegate) {
		super(delegate);
	}

	@Override
	public String getFormatedURL(Integer... params) {
		return String.format(server_url, params[0]);
	}

	@Override
	public Type getElementType() {
		return new TypeToken<List<Veiculo>>() {}.getType();
	}

	@Override
	public List<Veiculo> onErrorReturn() {
		return new ArrayList<Veiculo>();
	}
}
