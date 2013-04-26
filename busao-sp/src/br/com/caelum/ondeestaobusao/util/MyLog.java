package br.com.caelum.ondeestaobusao.util;

import android.util.Log;

public class MyLog {
	public static void i(Object o) {
		Log.i("BUSAO SP", o.toString());
	}
	
	public static void e(Object o) {
		Log.e("BUSAO SP", o.toString());
	}
}
