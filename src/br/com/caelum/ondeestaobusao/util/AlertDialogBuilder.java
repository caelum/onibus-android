package br.com.caelum.ondeestaobusao.util;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogBuilder {
	public static AlertDialog.Builder builder(Context ctx) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
		dialog.setTitle("Ocorreu um erro :(");
		dialog.setMessage("Infelizmente não foi possível se comunicar com o servidor!");
		dialog.setCancelable(true);
		return dialog;
	}

}
