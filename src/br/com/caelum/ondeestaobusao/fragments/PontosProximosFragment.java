package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.gps.GPSObserver;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

public class PontosProximosFragment extends Fragment implements GPSObserver, AsyncResultDelegate<List<Ponto>> {

	private final GPSControl gps;
	private BusaoActivity activity;
	private PontoExpandableListView pontosExpandableListView;
	private View menuBottom;

	public PontosProximosFragment(GPSControl gps) {
		this.gps = gps;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
		if (this.activity == null) {
			this.activity = (BusaoActivity) inflater.getContext();
	
			menuBottom = this.activity.findViewById(R.id.action_bottom);
	
			pontosExpandableListView = (PontoExpandableListView) inflater
					.inflate(R.layout.pontos_e_onibuses, parent, false);
			pontosExpandableListView.setVisibility(View.GONE);
			
			this.gps.registerObserver(this);
		}
		this.activity.atualizaNomeFragment(getTag());
		
		return pontosExpandableListView;
	}

	@Override
	public void callback(Coordenada coordenada) {
		new PontosEOnibusTask(this).execute(coordenada);
		activity.atualizaTextoDoProgress(R.string.buscando_pontos_proximos);
	}

	@Override
	public void dealWithResult(final List<Ponto> pontos) {
		pontosExpandableListView.setAdapter(new PontosEOnibusAdapter(pontos, activity));

		pontosExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);

				getFragmentManager().beginTransaction()
						.add(R.id.fragment_main, new MapaOnibusFragment(gps, onibus), onibus.getLetreiro())
						.addToBackStack(null).remove(PontosProximosFragment.this).commit();
				return false;
			}
		});
		pontosExpandableListView.setVisibility(View.VISIBLE);
		activity.escondeProgress();
	}

	@Override
	public void dealWithError() {
		activity.dealWithError();
	}

	@Override
	public void onPause() {
		super.onPause();
		menuBottom.setVisibility(View.GONE);
	}
	
	public void onResume() {
		super.onResume();
		menuBottom.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		ViewGroup parent = (ViewGroup) pontosExpandableListView.getParent();
		if (parent != null) {
			parent.removeView(pontosExpandableListView);
		}
	}

}
