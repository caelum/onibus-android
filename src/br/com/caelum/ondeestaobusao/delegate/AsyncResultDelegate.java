package br.com.caelum.ondeestaobusao.delegate;


public interface AsyncResultDelegate <T> {
	
	public void dealWithResult(T result);
	
	public void dealWithError();
	
}
