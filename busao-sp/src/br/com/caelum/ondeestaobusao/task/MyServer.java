package br.com.caelum.ondeestaobusao.task;

import android.os.Build;

public class MyServer {
	private static String uri;
	
	static {
		if (taNoEmulador()) {
			uri = "http://10.0.2.2:3000/%s";
		} else {
			uri = "http://ondeestaoalbi2.herokuapp.com/%s";
		}
	}
	
	public static String uriFor(String value) {
		return String.format(uri, value);
	}

	private static boolean taNoEmulador() {
		return Build.PRODUCT.equals("google_sdk")
				|| Build.PRODUCT.equals("sdk");
	}
	
}
