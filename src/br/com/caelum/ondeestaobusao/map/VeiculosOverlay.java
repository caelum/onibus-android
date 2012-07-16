package br.com.caelum.ondeestaobusao.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import br.com.caelum.ondeestaobusao.activity.R;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class VeiculosOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;

	public VeiculosOverlay(Context context) {
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.ic_bus_real_time)));
		mContext = context;
		populate();
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
