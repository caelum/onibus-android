package br.com.caelum.ondeestaobusao.gps;

import br.com.caelum.ondeestaobusao.model.Coordenada;

public interface LocationObserver {
	void callback(Coordenada coordenada); 
}
