package br.com.caelum.ondeestaobusao.fragments;

import android.support.v4.app.Fragment;
import br.com.caelum.ondeestaobusao.activity.R;

public abstract class BaseFragment extends Fragment{
	
	public void mudaFragment(Fragment destino) {
		getFragmentManager().beginTransaction()
			.add(R.id.fragment_main, destino, destino.getClass().getName())
			.addToBackStack(null)
			.remove(this)
		.commit();
		
	}
	
//	public abstract View doOnTargetView();
	
}
