package br.com.caelum.ondeestaobusao.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.caelum.ondeestaobusao.model.Coordenada;

public class CoordenadaMapsParser {
	private final String json;

	public CoordenadaMapsParser(String json) {
		this.json = json;
	}

	public Coordenada extract() {
		try {
			JSONArray jsonArray = new JSONObject(json).getJSONArray("results");
			
			JSONObject c = jsonArray.getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location");
				
			return new Coordenada(c.getDouble("lat"), c.getDouble("lng"));
		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}
}
