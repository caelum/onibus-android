package br.com.caelum.ondeestaobusao.gps;

import java.util.Collection;
import java.util.LinkedList;

import android.location.Location;
import br.com.caelum.ondeestaobusao.model.Coordenada;

public class LocationControl {
	private Collection<LocationObserver> observers = new LinkedList<LocationObserver>();
	private Coordenada atual;

	public void makeUseLocation(Location location) {
		if (location != null) {
			this.atual = new Coordenada(location.getLatitude(), location.getLongitude());
			for (LocationObserver observer : observers) {
				observer.callback(atual);
			}
		} else {
			//TODO TRATAR O ERRO AQUI
		}
	}

	public void registerObserver(LocationObserver observer) {
		if (atual != null) {
			observer.callback(atual);
		}
		this.observers.add(observer);
	}
	
	public void unRegisterObserver(LocationObserver observer) {
		this.observers.remove(observer);
	}
	
	public Coordenada getAtual() {
		return atual;
	}

}
