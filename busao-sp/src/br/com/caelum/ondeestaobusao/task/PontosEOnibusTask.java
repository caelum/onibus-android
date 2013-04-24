package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.eventos.EventoPontosEncontrados;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.gson.reflect.TypeToken;

public class PontosEOnibusTask extends BaseCachedGetJsonAsyncTask<Coordenada, ArrayList<Ponto>> implements LongRunningTask{
	private final String server_url = "http://ondeestaoalbi2.herokuapp.com/onibusesNosPontosProximos.json?lat=%s&long=%s";
	private Context context;
	private String mensagemDeFalha;

	public PontosEOnibusTask(Cache cache, Context context) {
		super(cache);
		this.context = context;
		this.mensagemDeFalha = context.getResources().getString(R.string.tente_novamente);
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
			EventoPontosEncontrados.notifica(context, result);
		} else {
			EventoPontosEncontrados.notificaFalha(context, mensagemDeFalha);
		}
	}

	@Override
	public void cancel() {
		this.cancel(true);
	}
}
