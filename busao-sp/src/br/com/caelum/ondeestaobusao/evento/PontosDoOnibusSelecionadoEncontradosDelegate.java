package br.com.caelum.ondeestaobusao.evento;

import java.util.ArrayList;

import br.com.caelum.ondeestaobusao.model.Ponto;
import android.content.Context;

public interface PontosDoOnibusSelecionadoEncontradosDelegate {

	Context getContext();

	void lidaComFalha(String mensagem);

	void lidaCom(ArrayList<Ponto> pontos);

}
