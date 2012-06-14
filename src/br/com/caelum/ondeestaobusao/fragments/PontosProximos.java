package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.generic.fragment.GenericFragment;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

public class PontosProximos implements GenericFragment, GPSObserver, AsyncResultDelegate<List<Ponto>> {

	private final GPSControl gps;
	private Activity activity;
	private PontoExpandableListView pontosExpandableListView;

	public PontosProximos(GPSControl gps) {
		this.gps = gps;
		this.gps.registerObserver(this);
	}

	@Override
	public View onCreateView(Activity activity, ViewGroup container) {
		this.activity = activity;
		pontosExpandableListView = (PontoExpandableListView) activity.getLayoutInflater().inflate(R.layout.pontos_e_onibuses, container, false);
		return pontosExpandableListView;
	}

	@Override
	public void callback(Coordenada coordenada) {
		new PontosEOnibusTask(this).execute(coordenada);
	}

	@Override
	public void dealWithResult(List<Ponto> pontos) {
		pontosExpandableListView.setAdapter(new PontosEOnibusAdapter(pontos, activity));

		pontosExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				// TODO Auto-generated method stub

				return false;
			}
		});

	}

	@Override
	public void dealWithError() {
		// TODO Auto-generated method stub
		
	}

}
