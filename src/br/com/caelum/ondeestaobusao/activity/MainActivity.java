package br.com.caelum.ondeestaobusao.activity;

import android.os.Bundle;
import br.com.caelum.ondeestaobusao.widget.AppRater;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class MainActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.main_novo);
		
		AppRater.app_launched(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_split_principal, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
