package br.com.caelum.ondeestaobusao.evento.delegate;

import java.util.ArrayList;

import br.com.caelum.ondeestaobusao.model.Ponto;

public interface PontosEncontradosDelegate {

	void lidaComPontosProximos(ArrayList<Ponto> pontos);

	void lidaComFalha(String mensagem);

}
