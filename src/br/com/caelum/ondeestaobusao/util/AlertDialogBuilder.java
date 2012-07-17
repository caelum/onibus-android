package br.com.caelum.ondeestaobusao.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class AlertDialogBuilder {
	public static AlertDialog builder(Context ctx) {
		AlertDialog dialog = new Builder(ctx).create();
		dialog.setTitle("Ocorreu um erro :(");
		dialog.setMessage("Infelizmente não foi possível se comunicar com o servidor!");
		return dialog;
	}
}
