package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.MainActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.activity.application.BusaoApplication;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.BuscaPontosDoOnibusTask;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

public class PontosProximosFragment extends Fragment {
	private PontoExpandableListView pontosExpandableListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.onibuses_proximos, null);
		
		pontosExpandableListView = (PontoExpandableListView) view.findViewById(R.id.listPonto);
		
		List<Ponto> pontos = ((MainActivity)getActivity()).getPontos();
		if (pontos!= null) {
			colocaNaTela(pontos);
		}
		
		return view;
	}
	
	public void colocaNaTela(final List<Ponto> pontos) {
		pontosExpandableListView.setAdapter(new PontosEOnibusAdapter(pontos, getActivity()));

		pontosExpandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);

				BuscaPontosDoOnibusTask task = new BuscaPontosDoOnibusTask((BusaoApplication) getActivity().getApplication());
				task.execute(onibus.getId());
				
				//TODO ...
				
				return false;
			}
		});
	}
	
}
