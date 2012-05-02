package br.com.caelum.ondeestaobusao.task;

import java.lang.reflect.Type;

public interface GetJsonResolver<Params, Result> {
	
	void doOnPostExecute(Result result);
	
	String getFormatedURL(Params... params);
	
	Type getElementType();
	
	void doOnError(Exception e);
	
	Result onErrorReturn();
}
