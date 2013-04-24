package br.com.caelum.ondeestaobusao.activity.application;

import android.app.Application;
import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.gps.CentralNotificacoes;
import br.com.caelum.ondeestaobusao.model.Coordenada;

public class BusaoApplication extends Application {
	private CentralNotificacoes location;
	private Cache cache;
	private Coordenada ultimaLocalizacao;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		cache = new Cache();
		
		location = new CentralNotificacoes(this);
		
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
