package br.com.caelum.ondeestaobusao.evento;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.util.MyLog;

public class PontosDoOnibusSelecionadoEncontrados extends BroadcastReceiver implements Evento{

	private static final String FALHOU = "falhou";
	private static final String MENSAGEM_FALHA = "mensagemFalha";
	private static final String PONTOS = "pontos";
	private static final String PONTOS_ENCONTRADOS = "pontos-do-onibus-selecionado-encontrado";
	private static final String PONTOS_NAO_ENCONTRADOS = "pontos-do-onibus-selecionado-nao-encontrados";
	
	private PontosDoOnibusSelecionadoEncontradosDelegate delegate;

	@Override
	@SuppressWarnings("unchecked")
	public void onReceive(Context context, Intent intent) {
		if (intent.getBooleanExtra(FALHOU, false)) {
			MyLog.i("RECEBIDA MENSAGEM DE falha! para delegate"+delegate);
			
			delegate.lidaComFalha((String) intent.getSerializableExtra(MENSAGEM_FALHA));
		} else {
			MyLog.i("RECEBIDA MENSAGEM DE SUCESSO! para delegate"+delegate);
			ArrayList<Ponto> pontos = (ArrayList<Ponto>) intent.getSerializableExtra(PONTOS);			
			delegate.lidaCom(pontos);
		}
		
	}
	public static void notifica(Context context, ArrayList<Ponto> pontos) {
		Intent intent = new Intent(PONTOS_ENCONTRADOS);

		intent.putExtra(PONTOS, pontos);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	public static Evento registraObservador(PontosDoOnibusSelecionadoEncontradosDelegate delegate){
		PontosDoOnibusSelecionadoEncontrados receiver = new PontosDoOnibusSelecionadoEncontrados();
		receiver.delegate = delegate;
		
		LocalBroadcastManager.getInstance(delegate.getContext())
			.registerReceiver(receiver, new IntentFilter(PONTOS_ENCONTRADOS));
		
		LocalBroadcastManager.getInstance(delegate.getContext())
			.registerReceiver(receiver, new IntentFilter(PONTOS_NAO_ENCONTRADOS));
		
		return receiver;
	}
	public static void notificaFalha(Context context, String mensagem) {
		Intent intent = new Intent(PONTOS_NAO_ENCONTRADOS);
		intent.putExtra(MENSAGEM_FALHA, mensagem);
		intent.putExtra(FALHOU, true);
		
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	public void unregister(Context context) {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
	}
	
}
