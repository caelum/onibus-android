package br.com.caelum.ondeestaobusao.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.gps.GPSObserver;

public abstract class BaseFragment extends Fragment implements HeaderChanger {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		View v = onCreateView(inflater, viewGroup);
		updateHeader(viewGroup.getRootView());
		
		if (this instanceof GPSObserver) {
			BusaoActivity activity =  (BusaoActivity) getActivity();
			activity.getGps().registerObserver((GPSObserver) this);
		}
		
		return v;
	}

	public abstract View onCreateView(LayoutInflater inflater, ViewGroup viewGroup);
	
	public void vaiPara(Fragment destino, String titulo) {
		getFragmentManager().beginTransaction()
			.add(R.id.fragment_main, destino, this.getClass().getName())
			.addToBackStack(null)
			.remove(this)
		.commit();
	}
	
	@Override
	public void onDestroyView() {
		if (this instanceof GPSObserver) {
			BusaoActivity activity =  (BusaoActivity) getActivity();
			activity.getGps().unRegisterObserver((GPSObserver) this);
		}
		super.onDestroyView();
	}
	
}
