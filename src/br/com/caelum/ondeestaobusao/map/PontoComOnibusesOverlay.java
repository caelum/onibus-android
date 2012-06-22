package br.com.caelum.ondeestaobusao.map;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.fragments.MapaComPontosEOnibusesFragment;
import br.com.caelum.ondeestaobusao.model.Ponto;

import com.google.android.maps.ItemizedOverlay;


public class PontoComOnibusesOverlay extends ItemizedOverlay<PontoOverlayItem> {

	private ArrayList<PontoOverlayItem> mOverlays = new ArrayList<PontoOverlayItem>();
	private BusaoActivity activity;
	private MapaComPontosEOnibusesFragment fragment;

	public PontoComOnibusesOverlay(Drawable drawable) {
		super(drawable);
	}

	public PontoComOnibusesOverlay(Activity activity, Fragment fragment) {
		super(boundCenterBottom(activity.getResources().getDrawable(R.drawable.ic_pin_bus)));
		this.fragment = (MapaComPontosEOnibusesFragment) fragment;
		this.activity = (BusaoActivity) activity;
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

		AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		dialog.setTitle("Localização do ponto: ");
		dialog.setMessage(ponto.getDescricao());
		dialog.setCancelable(true);
		dialog.setPositiveButton(ponto.getOnibuses().size() + " ônibus", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				fragment.finaliza(ponto);
			}
		});
		dialog.show();
		return true;
	}

	public void clear() {
		mOverlays.clear();
	}
}
