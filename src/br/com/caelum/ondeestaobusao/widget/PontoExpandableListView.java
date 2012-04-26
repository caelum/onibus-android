package br.com.caelum.ondeestaobusao.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;
import br.com.caelum.ondeestaobusao.util.CalculatePixelInDP;

public class PontoExpandableListView extends ExpandableListView {

	private DisplayMetrics metrics;

	public PontoExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		metrics = new DisplayMetrics();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		setIndicatorBounds(width - CalculatePixelInDP.getDpsFrom(50, getResources()), width - CalculatePixelInDP.getDpsFrom(10, getResources()));
	}
	
}
