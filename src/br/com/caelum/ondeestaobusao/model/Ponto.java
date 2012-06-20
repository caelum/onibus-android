package br.com.caelum.ondeestaobusao.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.android.maps.OverlayItem;

@SuppressWarnings("serial")
public class Ponto implements Serializable {
	private String nome;
	private String descricao;
	private Double distancia;
	private ArrayList<Onibus> onibuses;
	private Coordenada coordenada;
	
	public Ponto(String nome, Coordenada coordenada, String descricao, Double distancia) {
		this.nome = nome;
		this.coordenada = coordenada;
		this.descricao = descricao;
		this.distancia = distancia;
	}
	public Ponto(String nome, Coordenada coordenada, ArrayList<Onibus> onibuses, String descricao, Double distancia) {
		this(nome, coordenada, descricao, distancia);
		this.onibuses = onibuses;
	}
	
	public OverlayItem toOverlayItem() {
		return new OverlayItem(
				getCoordenada().toGeoPoint(), "Localização do ponto:", getDescricao());
	}
	
	@Override
	public String toString() {
		return nome +coordenada.toString();
	}

	public String getNome() {
		return nome;
	}
	
	public Coordenada getCoordenada() {
		return coordenada;
	}

	public ArrayList<Onibus> getOnibuses() {
		return onibuses;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getDistancia() {
		return "Distância: "+String.format("%.0f", distancia * 1000)+"m";
	}
}
