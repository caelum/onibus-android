package br.com.caelum.ondeestaobusao.cache;

import java.util.HashMap;

public class Cache {
	
	private HashMap<String, String> cached;
	
	public Cache() {
		this.cached = new HashMap<String, String>();
	}
	
	public void addCache(String url, String json) {
		cached.put(url, json);
	}
	
	public String get(String url) {
		return cached.get(url);
	}

}
