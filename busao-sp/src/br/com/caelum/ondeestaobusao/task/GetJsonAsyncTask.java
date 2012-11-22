package br.com.caelum.ondeestaobusao.task;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import br.com.caelum.ondeestaobusao.cache.Cache;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class GetJsonAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {
	private final AsyncResultDelegate<Result> delegate;
	private IOException exception;
	private Cache cache;

	public GetJsonAsyncTask(AsyncResultDelegate<Result> delegate) {
		this.delegate = delegate;
		this.cache = null;
	}
	
	public GetJsonAsyncTask(Cache cache, AsyncResultDelegate<Result> delegate) {
		this(delegate);
		this.cache = cache;
	}
	
	protected abstract Result onErrorReturn();

	protected abstract String getFormatedURL(Params... params);
	
	protected abstract Type getElementType();
	
	@Override
	protected final Result doInBackground(Params... params) {
		try {
			String json = getJsonFromServer(params);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			
			return gson.fromJson(json, getElementType());
		} catch (IOException e) {
			this.exception = e;
			return onErrorReturn();
		}
	}

	private String getJsonFromServer(Params... params) throws IOException {
		String url = getFormatedURL(params);
		
		if (cache != null) {
			String json = cache.get(url);
			if (json != null) {
				return json;
			}
		}
		
		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);
		
		String json = EntityUtils.toString(response.getEntity());
		
		if (cache != null) {
			cache.addCache(url, json);
		}
		
		return json;

	}
	
	@Override
	protected final void onPostExecute(Result result) {
		if (exception == null) {
			delegate.dealWithResult(result);
		} else {
			delegate.dealWithError();
		}
	}

}
