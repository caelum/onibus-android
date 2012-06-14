//package br.com.caelum.ondeestaobusao.map;
//
//import java.util.ArrayList;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import br.com.caelum.ondeestaobusao.model.Ponto;
//
//import com.google.android.maps.ItemizedOverlay;
//
//
//public class PontoClickOverlay extends ItemizedOverlay<PontoOverlayItem> {
//
//	private ArrayList<PontoOverlayItem> mOverlays = new ArrayList<PontoOverlayItem>();
//	private MostraPontosActivity mContext;
//
//	public PontoClickOverlay(Drawable drawable) {
//		super(drawable);
//	}
//
//	public PontoClickOverlay(Drawable defaultMarker, MostraPontosActivity context) {
//		super(boundCenterBottom(defaultMarker));
//		mContext = context;
//	}
//
//	@Override
//	protected PontoOverlayItem createItem(int i) {
//		return mOverlays.get(i);
//	}
//
//	@Override
//	public int size() {
//		return mOverlays.size();
//	}
//
//	public void addOverlay(PontoOverlayItem overlay) {
//		mOverlays.add(overlay);
//		populate();
//	}
//
//	@Override
//	protected boolean onTap(int index) {
//		final PontoOverlayItem item = mOverlays.get(index);
//		final Ponto ponto = item.getPonto();
//
//		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//		dialog.setTitle("Localização do ponto: ");
//		dialog.setMessage(ponto.getDescricao());
//		dialog.setCancelable(true);
//		dialog.setPositiveButton(ponto.getOnibuses().size() + " ônibus", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(mContext, ListOnibusActivity.class);
//
//				intent.putExtra("ponto", ponto);
//				mContext.startActivity(intent);
//			}
//		});
//		dialog.show();
//		return true;
//	}
//
//	public void clear() {
//		mOverlays.clear();
//	}
//}
