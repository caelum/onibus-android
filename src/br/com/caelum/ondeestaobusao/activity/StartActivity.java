package br.com.caelum.ondeestaobusao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class StartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
        	setContentView(new View(this));
        	
        	new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					startActivity(new Intent(StartActivity.this, ListPontosAndOnibusActivity.class));
					finish();
				}
			}, 5000);
        }
    }
}