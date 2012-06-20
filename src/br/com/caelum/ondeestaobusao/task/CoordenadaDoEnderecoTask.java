package br.com.caelum.ondeestaobusao.task;

import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.parser.CoordenadaMapsParser;

public class CoordenadaDoEnderecoTask extends AsyncTask<String, Void, Coordenada>{
	private final String url = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=%s";
	private boolean erroNaExecucao = false;
	private final AsyncResultDelegate<Coordenada> delegate;
	
	public CoordenadaDoEnderecoTask(AsyncResultDelegate<Coordenada> delegate) {
		this.delegate = delegate;
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Coordenada doInBackground(String... params) {
		try {
			String enderecoEncoded = URLEncoder.encode(params[0]);
			String requestURI = String.format(url, enderecoEncoded);
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(requestURI);
			
			HttpResponse response = httpClient.execute(get);
			String json = EntityUtils.toString(response.getEntity());
			
			return new CoordenadaMapsParser(json).extract();
			
		} catch (Exception e) {
			erroNaExecucao = true;
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Coordenada result) {
		if (!erroNaExecucao) {
			delegate.dealWithResult(result);
		} else {
			delegate.dealWithError();
		}
	}
	
	

}
