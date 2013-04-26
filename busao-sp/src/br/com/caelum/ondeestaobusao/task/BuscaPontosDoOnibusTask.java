package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.evento.PontosDoOnibusSelecionadoEncontrados;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.util.CancelableAssynTask;

import com.google.gson.reflect.TypeToken;

public class BuscaPontosDoOnibusTask extends BaseCachedGetJsonAsyncTask<Long, List<Ponto>> implements LongRunningTask {
	private final String server_url = "http://ondeestaoalbi2.herokuapp.com/itinerarioDoOnibus.json?onibus=%s";
	private BusaoApplication application;
	
	public BuscaPontosDoOnibusTask(BusaoApplication application) {
		super(application.getCache());
		this.application = application;
		application.add(this);
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
	protected void onPostExecute(List<Ponto> pontos) {
		
		if (TaskStatus.OK.equals(getEndBackgroundStatus())) {
			PontosDoOnibusSelecionadoEncontrados.notifica(application, (ArrayList<Ponto>) pontos);
		} else {
			PontosDoOnibusSelecionadoEncontrados.notificaFalha(application, "Pontos do ônibus não encontrado");
		}
		application.remove(this);
	}

	@Override
	public void cancel() {
		CancelableAssynTask.cancel(this);
		application.remove(this);
	}
}
