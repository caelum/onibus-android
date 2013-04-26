package br.com.caelum.ondeestaobusao.activity;

import java.io.Serializable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import br.com.caelum.ondeestaobusao.fragments.PontosProximosFragment;
import br.com.caelum.ondeestaobusao.fragments.ProgressFragment;

public enum EstadoMainActivity implements Serializable {
	SEM_LOCALIZACAO {
		@Override
		EstadoMainActivity proximo() {
			return BUSCANDO_BASEADO_NOVA_LOCALIZACAO;
		}

		@Override
		public EstadoMainActivity executaEvolucao(MainActivity activity) {
			FragmentManager manager = activity.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.principal_unico, ProgressFragment.comMensagem(R.string.carregando_gps));
			transaction.commit();
			
			return proximo();
		}

	}, BUSCANDO_BASEADO_NOVA_LOCALIZACAO {
		@Override
		EstadoMainActivity proximo() {
			return COM_PONTOS;
		}

		@Override
		public EstadoMainActivity executaEvolucao(MainActivity activity) {
			FragmentManager manager = activity.getSupportFragmentManager();
			
			Fragment fragment = manager.findFragmentById(R.id.principal_unico);
			if (fragment instanceof ProgressFragment) {
				((ProgressFragment) fragment).mudaMensagem(R.string.buscando_pontos_proximos);
			} else {
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.replace(R.id.principal_unico, ProgressFragment.comMensagem(R.string.buscando_pontos_proximos));
				transaction.commit();
			}
			
			
			return proximo();
		}

	}, COM_PONTOS {
		@Override
		EstadoMainActivity proximo() {
			return COM_PONTOS;
		}

		@Override
		public EstadoMainActivity executaEvolucao(MainActivity activity) {
			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.principal_unico, new PontosProximosFragment(), PontosProximosFragment.class.getName());
			transaction.commit();
			
			return proximo();
		}
	};
	
	abstract EstadoMainActivity proximo();
	
	public abstract EstadoMainActivity executaEvolucao(MainActivity activity);
	
	public static EstadoMainActivity inicio() {
		return SEM_LOCALIZACAO;
	}

}

