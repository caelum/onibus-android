package br.com.caelum.ondeestaobusao.progressbar;

import br.com.caelum.ondeestaobusao.activity.R;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class ProgressBarAdministrator {
	
	private View progressBarLayout;
	private TextView progressBarText;
	private Activity activity;

	public ProgressBarAdministrator(Activity activity) {
		this.activity = activity;
		progressBarLayout = activity.findViewById(R.id.progress_bar);
		progressBarText = (TextView) progressBarLayout.findViewById(R.id.progress_text);
	}
	
	public void hide() {
		progressBarLayout.setVisibility(View.GONE);
	}
	
	public void show() {
		progressBarLayout.setVisibility(View.VISIBLE);
	}
	
	public void showWithText(int stringId) {
		show();
		changedText(stringId);
	}
	
	public void changedText(int stringId) {
		String text = activity.getResources().getString(stringId);
		progressBarText.setText(text);
	}

}
