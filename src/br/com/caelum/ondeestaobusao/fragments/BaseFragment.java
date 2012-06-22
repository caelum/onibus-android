package br.com.caelum.ondeestaobusao.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.caelum.ondeestaobusao.activity.R;

public abstract class BaseFragment extends Fragment implements HeaderChanger {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		View v = onCreateView(inflater, viewGroup);
		updateHeader(viewGroup.getRootView());
		
		return v;
	}

	public abstract View onCreateView(LayoutInflater inflater, ViewGroup viewGroup);
	
	public void vaiPara(Fragment destino, String titulo) {
		getFragmentManager().beginTransaction()
			.add(R.id.fragment_main, destino, destino.getClass().getName())
			.addToBackStack(null)
			.remove(this)
		.commit();
	}
	
}
