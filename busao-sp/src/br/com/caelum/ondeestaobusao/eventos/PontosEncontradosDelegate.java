package br.com.caelum.ondeestaobusao.eventos;

import java.util.ArrayList;

import br.com.caelum.ondeestaobusao.model.Ponto;

public interface PontosEncontradosDelegate {

	void lidaCom(ArrayList<Ponto> pontos);

	void lidaComFalha(String mensagem);

}
