package br.com.caelum.ondeestaobusao.task;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import br.com.caelum.ondeestaobusao.delegate.GetJsonDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetJsonAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {
	private final GetJsonDelegate<Params, Result> delegate;
	private IOException exception;

	public GetJsonAsyncTask(GetJsonDelegate<Params, Result> delegate) {
		this.delegate = delegate;
	}

	@Override
	protected Result doInBackground(Params... params) {
		try {
			String json = getJsonFromServer(params);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			
			return gson.fromJson(json, delegate.getElementType());
		} catch (IOException e) {
			this.exception = e;
			return delegate.onErrorReturn();
		}
	}

	private String getJsonFromServer(Params... params) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(delegate.getFormatedURL(params));
		HttpResponse response = httpclient.execute(httpGet);

		return EntityUtils.toString(response.getEntity());

	}

	@Override
	protected void onPostExecute(Result result) {
		if (exception == null) {
			delegate.doOnPostExecute(result);
		} else {
			delegate.doOnError(exception);
		}
	}

}
