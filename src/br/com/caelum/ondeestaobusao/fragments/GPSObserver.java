package br.com.caelum.ondeestaobusao.fragments;

import br.com.caelum.ondeestaobusao.model.Coordenada;

public interface GPSObserver {
	void callback(Coordenada coordenada); 
}
