package br.com.caelum.ondeestaobusao.gps;

import java.util.Collection;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import br.com.caelum.ondeestaobusao.model.Coordenada;

public class CentralNotificacoes {
	private Collection<LocationObserver> observers = new LinkedList<LocationObserver>();
	private Coordenada atual;
	private final Context context;

	public CentralNotificacoes(Context context) {
		this.context = context;
	}

	public void makeUseLocation(Location location) {
		if (location != null) {
			this.atual = new Coordenada(location.getLatitude(), location.getLongitude());
			for (LocationObserver observer : observers) {
				observer.mudouLocalizacaoPara(atual);
			}
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Ocorreu um erro :(");
			dialog.setMessage("Infelizmente não foi possível obter sua localização.");
			dialog.setCancelable(true);
			dialog.setPositiveButton("Quero habilitar o GPS",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        Intent gpsIntent = new Intent(
	                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                        context.startActivity(gpsIntent);
	                    }

	                });
	        dialog.setNegativeButton("Agora não",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	//TODO ?
	                    }
	                });
	        dialog.show();
		}
	}

	public void registerObserver(LocationObserver observer) {
		if (atual != null) {
			observer.mudouLocalizacaoPara(atual);
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
