package br.com.caelum.ondeestaobusao.task;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetJsonAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result>{
	private final GetJsonResolver<Params, Result> resolver;

	public GetJsonAsyncTask(GetJsonResolver<Params, Result> resolver) {
		this.resolver = resolver;
	}
	
	@Override
	protected Result doInBackground(Params... params) {
		String json = getJsonFromServer(params);
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
		return gson.fromJson(json, resolver.getElementType());
	}
	
	private String getJsonFromServer(Params... params) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			
			HttpGet httpGet = new HttpGet(resolver.getFormatedURL(params));
			HttpResponse response = httpclient.execute(httpGet);

			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onPostExecute(Result result) {
		resolver.doOnPostExecute(result);
	}

}
