package br.com.caelum.ondeestaobusao.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import br.com.caelum.ondeestaobusao.activity.R;

public class AppRater {
    private final static String APP_PACKAGE_NAME = "br.com.caelum.ondeestaobusao.activity";
    
    private final static int DAYS_UNTIL_PROMPT = 7;
    private final static int LAUNCHES_UNTIL_PROMPT = 10;
    
    private static SharedPreferences.Editor editor;

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        
        if (prefs.getBoolean("naoMostrarNuncaMais", false)) { return ; }
        
        editor = prefs.edit();
        
        long launch_count = incrementaContadorDeExecucao(prefs);
        Long date_firstLaunch = dataDaPrimeiraExecucao(prefs);
        
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            	
            	exibeDialog(mContext);
            }
        }
        editor.commit();
    }

	private static void exibeDialog(final Context mContext) {
		final Dialog dialog = new Dialog(mContext,R.style.RateMeDialog);
		dialog.setTitle(R.string.nos_de_seu_feedback);
		dialog.setContentView(R.layout.feedback);
		
		Button market = (Button) dialog.findViewById(R.id.vaiParaOMarket);
		market.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
				dialog.dismiss();
			}
		});
		
		Button dismiss = (Button) dialog.findViewById(R.id.lembrarMaisTarde);
		dismiss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		Button noTks = (Button) dialog.findViewById(R.id.naoObrigado);
		noTks.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editor != null) {
					editor.putBoolean("naoMostrarNuncaMais", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	

	private static Long dataDaPrimeiraExecucao(SharedPreferences prefs) {
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
		return date_firstLaunch;
	}

	private static long incrementaContadorDeExecucao(SharedPreferences prefs) {
		long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);
		return launch_count;
	}
}