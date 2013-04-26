package br.com.caelum.ondeestaobusao.activity.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.gps.CentralNotificacoes;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.task.LongRunningTask;

public class BusaoApplication extends Application {
	private List<LongRunningTask> tasks = new ArrayList<LongRunningTask>();
	private CentralNotificacoes location;
	private Cache cache;
	private Coordenada ultimaLocalizacao;
	
	@Override
	public void onCreate() {
		super.onCreate();
		cache = new Cache();
		location = new CentralNotificacoes(this);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		for (LongRunningTask task : this.tasks) {
			task.cancel();
		}
	}
	
	public void add(LongRunningTask task) {
		tasks.add(task);
	}
	
	public void remove(LongRunningTask task) {
		tasks.remove(task);
	}
	
	public void setCentralNotificacoes(CentralNotificacoes control) {
		this.location = control;
	}

	public CentralNotificacoes getCentralNotificacoes() {
		return location;
	}

	public Cache getCache() {
		return cache;
	}

	public void setUltimaLocalizacao(Coordenada coordenada) {
		this.ultimaLocalizacao = coordenada;
	}
	
	public Coordenada getUltimaLocalizacao() {
		return ultimaLocalizacao;
	}
}
