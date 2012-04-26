package br.com.caelum.ondeestaobusao.util;

import android.content.res.Resources;

public class CalculatePixelInDP {
	public static int getDpsFrom(float pixels, Resources resources) {
		final float scale = resources.getDisplayMetrics().density;
		return (int) (pixels * scale + 0.5f);
	}
}
