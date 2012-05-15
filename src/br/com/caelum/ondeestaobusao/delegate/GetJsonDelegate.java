package br.com.caelum.ondeestaobusao.delegate;

import java.lang.reflect.Type;

public interface GetJsonDelegate<Params, Result> {
	
	void doOnPostExecute(Result result);
	
	String getFormatedURL(Params... params);
	
	Type getElementType();
	
	void doOnError(Exception e);
	
	Result onErrorReturn();
}
