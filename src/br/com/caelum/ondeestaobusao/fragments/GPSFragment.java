package br.com.caelum.ondeestaobusao.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.gps.GPSObserver;

public abstract class GPSFragment extends BaseFragment implements GPSObserver {
	private final GPSControl gps;

	public GPSFragment(GPSControl gps) {
		this.gps = gps;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		View view = super.onCreateView(inflater, viewGroup, bundle);
		this.gps.registerObserver(this);
		
		return view;
	}
	
	@Override
	public void onDestroyView() {
		gps.unRegisterObserver(this);
		super.onDestroyView();
	}
}
