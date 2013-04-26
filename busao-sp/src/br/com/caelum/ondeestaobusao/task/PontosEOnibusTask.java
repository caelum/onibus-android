package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.evento.PontosProximosEncontrados;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosEOnibusTask extends BaseCachedGetJsonAsyncTask<Coordenada, ArrayList<Ponto>> implements LongRunningTask{
	private final String server_url = "http://ondeestaoalbi2.herokuapp.com/onibusesNosPontosProximos.json?lat=%s&long=%s";
	private String mensagemDeFalha;
	private BusaoApplication application;

	public PontosEOnibusTask(BusaoApplication application) {
		super(application.getCache());
		this.application = application;
		this.mensagemDeFalha = this.application.getResources().getString(R.string.tente_novamente);
		
		application.add(this);
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
	public ArrayList<Ponto> onErrorReturn() {
		return new ArrayList<Ponto>();
	}
	
	@Override
	protected void onPostExecute(ArrayList<Ponto> result) {
		if (TaskStatus.OK.equals(getEndBackgroundStatus())) {
			PontosProximosEncontrados.notifica(application, result);
		} else {
			PontosProximosEncontrados.notificaFalha(application, mensagemDeFalha);
		}
		application.remove(this);
	}

	@Override
	public void cancel() {
		this.cancel(true);
		application.remove(this);
	}
}
