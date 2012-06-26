package br.com.caelum.ondeestaobusao.fragments;

import java.util.List;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import br.com.caelum.ondeestaobusao.activity.BusaoActivity;
import br.com.caelum.ondeestaobusao.activity.R;
import br.com.caelum.ondeestaobusao.adapter.PontosEOnibusAdapter;
import br.com.caelum.ondeestaobusao.constants.Extras;
import br.com.caelum.ondeestaobusao.delegate.AsyncResultDelegate;
import br.com.caelum.ondeestaobusao.gps.GPSControl;
import br.com.caelum.ondeestaobusao.model.Coordenada;
import br.com.caelum.ondeestaobusao.model.Onibus;
import br.com.caelum.ondeestaobusao.model.Ponto;
import br.com.caelum.ondeestaobusao.task.PontosEOnibusTask;
import br.com.caelum.ondeestaobusao.widget.PontoExpandableListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PontosProximosFragment extends GPSFragment implements AsyncResultDelegate<List<Ponto>> {
	private PontoExpandableListView pontosExpandableListView;
	private List<Ponto> pontos;
	private AsyncTask<Coordenada, Void, List<Ponto>> pontosEOnibusTask;

	public PontosProximosFragment(GPSControl gps) {
		super(gps);
		setHasOptionsMenu(true);	
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
		if (pontosExpandableListView == null) {
			pontosExpandableListView = (PontoExpandableListView) inflater.inflate(R.layout.pontos_e_onibuses, parent,
					false);
			pontosExpandableListView.setVisibility(View.GONE);
		}
		return pontosExpandableListView;
	}

	@Override
	public void callback(Coordenada coordenada) {
		pontosEOnibusTask = new PontosEOnibusTask(this).execute(coordenada);
		((BusaoActivity) getActivity()).atualizaTextoDoProgress(R.string.buscando_pontos_proximos);
	}

	@Override
	public void dealWithResult(final List<Ponto> pontos) {
		this.pontos = pontos;
		pontosExpandableListView.setAdapter(new PontosEOnibusAdapter(pontos, getActivity()));

		pontosExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Onibus onibus = pontos.get(groupPosition).getOnibuses().get(childPosition);

				MapaDoOnibusFragment mapaDoOnibusFragment = (MapaDoOnibusFragment) getFragmentManager()
						.findFragmentByTag(MapaDoOnibusFragment.class.getName());

				if (mapaDoOnibusFragment == null) {
					mapaDoOnibusFragment = new MapaDoOnibusFragment(((BusaoActivity) getActivity()));
				}

				Bundle bundle = new Bundle();
				bundle.putSerializable(Extras.ONIBUS, onibus);

				mapaDoOnibusFragment.setArguments(bundle);

				PontosProximosFragment.this.vaiPara(mapaDoOnibusFragment, onibus.toString());

				return false;
			}
		});
		pontosExpandableListView.setVisibility(View.VISIBLE);
		((BusaoActivity) getActivity()).escondeProgress();
	}

	@Override
	public void dealWithError() {
		((BusaoActivity) getActivity()).dealWithError();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		super.onDestroyOptionsMenu();
		
		if (pontosEOnibusTask != null && Status.RUNNING.equals(pontosEOnibusTask.getStatus())) {
			pontosEOnibusTask.cancel(true);
		}

		ViewGroup parent = (ViewGroup) pontosExpandableListView.getParent();
		if (parent != null) {
			parent.removeView(pontosExpandableListView);
		}

	}

	public List<Ponto> getPontos() {
		return pontos;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_split_principal, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_mapa) {
			MapaComPontosEOnibusesFragment mapaComPontosEOnibusesFragment = (MapaComPontosEOnibusesFragment) getFragmentManager().findFragmentByTag(MapaComPontosEOnibusesFragment.class.getName());
			if (mapaComPontosEOnibusesFragment == null) {
				mapaComPontosEOnibusesFragment = new MapaComPontosEOnibusesFragment((BusaoActivity) getActivity(), pontos);
			}
			this.vaiPara(mapaComPontosEOnibusesFragment, mapaComPontosEOnibusesFragment.getClass().getName());
			return true;
		} else if (item.getItemId() == R.id.menu_atualizar) {
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void updateHeader(View view) {
		getSherlockActivity().getSupportActionBar().setTitle(getString(R.string.pontos_proximos));
		getSherlockActivity().getSupportActionBar().setSubtitle(null);
	}

	public void selecionaPonto(Ponto ponto) {
		if (pontos != null) {
			for (int i = 0; i < pontos.size(); i++) {
				if (ponto.equals(pontos.get(i))) {
					pontosExpandableListView.expandGroup(i);
				} else {
					if (pontosExpandableListView.isGroupExpanded(i)) {
						pontosExpandableListView.collapseGroup(i);
					}
				}
			}
		}

	}
}
