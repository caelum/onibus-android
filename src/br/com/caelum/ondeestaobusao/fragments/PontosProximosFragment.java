package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.generic.fragment.GenericFragment;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.gps.GPSObserver;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

public class PontosProximosFragment implements GenericFragment, GPSObserver, AsyncResultDelegate<List<Ponto>> {

	private final GPSControl gps;
	private BusaoActivity activity;
	private PontoExpandableListView pontosExpandableListView;
	private View menuBottom;

	public PontosProximosFragment(GPSControl gps) {
		this.gps = gps;
		this.gps.registerObserver(this);
	}

	@Override
	public View onCreateView(Activity activity, ViewGroup container) {
		this.activity = (BusaoActivity) activity;
		
		menuBottom = this.activity.findViewById(R.id.action_bottom);
		
		pontosExpandableListView = (PontoExpandableListView) activity.getLayoutInflater().inflate(R.layout.pontos_e_onibuses, container, false);
		pontosExpandableListView.setVisibility(View.GONE);
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
				
				activity.getGenericFragmentManager().replace(R.id.fragment_main, new MapaOnibusFragment(gps, onibus), true);
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
	public void finish() {
		menuBottom.setVisibility(View.GONE);
	}

	@Override
	public void resume() {
		menuBottom.setVisibility(View.VISIBLE);
	}

	@Override
	public String getName() {
		return "Pontos Próximos";
	}

}
