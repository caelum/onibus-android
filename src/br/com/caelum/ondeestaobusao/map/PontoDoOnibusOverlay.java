package br.com.caelum.ondeestaobusao.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import br.com.caelum.ondeestaobusao.activity.R;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class PontoDoOnibusOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public PontoDoOnibusOverlay(Drawable drawable) {
		super(drawable);
	}

	public PontoDoOnibusOverlay(Context context) {
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.ic_bus_stop)));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.setCancelable(true);
		dialog.show();
		return true;
	}
	
	public void clear() {
		mOverlays.clear();
	}
}
