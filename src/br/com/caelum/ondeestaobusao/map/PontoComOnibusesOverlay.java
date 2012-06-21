package br.com.caelum.ondeestaobusao.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.android.maps.ItemizedOverlay;


public class PontoComOnibusesOverlay extends ItemizedOverlay<PontoOverlayItem> {

	private ArrayList<PontoOverlayItem> mOverlays = new ArrayList<PontoOverlayItem>();
	private Context mContext;

	public PontoComOnibusesOverlay(Drawable drawable) {
		super(drawable);
	}

	public PontoComOnibusesOverlay(Context context) {
		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.ic_pin_bus)));
		mContext = context;
	}

	@Override
	protected PontoOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void addOverlay(PontoOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		final PontoOverlayItem item = mOverlays.get(index);
		final Ponto ponto = item.getPonto();

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("Localização do ponto: ");
		dialog.setMessage(ponto.getDescricao());
		dialog.setCancelable(true);
		dialog.setPositiveButton(ponto.getOnibuses().size() + " ônibus", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dialog.show();
		return true;
	}

	public void clear() {
		mOverlays.clear();
	}
}
