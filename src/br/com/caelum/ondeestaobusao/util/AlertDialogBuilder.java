package br.com.caelum.ondeestaobusao.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogBuilder {
	
	private final Activity activity;


	public AlertDialogBuilder(Activity activity) {
		this.activity = activity;
	}
	
	
	public AlertDialog build() {
		AlertDialog dialog = new Builder(activity).create();
		dialog.setTitle("Erro:");
		dialog.setMessage("Não foi possível se comunicar com o servidor");
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, activity.getResources().getString(android.R.string.ok), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				activity.onBackPressed();
			}
			
		});
		return dialog;
	}
}
